package com.github.vaerys.handlers;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.commands.cc.EditCC;
import com.github.vaerys.commands.cc.NewCC;
import com.github.vaerys.main.Globals;
import com.github.vaerys.main.Utility;
import com.github.vaerys.objects.OffenderObject;
import com.github.vaerys.pogos.GuildConfig;
import com.github.vaerys.templates.Command;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpamHandler {

    final static Logger logger = LoggerFactory.getLogger(SpamHandler.class);

    public static boolean catchWalls(CommandObject command) {
        if (!command.guild.config.stopSpamWalls) return false;
        if (command.message.length() < 800) return false;
        String message = command.message.getContent();
        if (StringUtils.startsWithIgnoreCase(message, new NewCC().getCommand(command))) return false;
        if (StringUtils.startsWithIgnoreCase(message, new EditCC().getCommand(command))) return false;
        List<String> chunks = Arrays.asList(message.split("(?<=\\G..........)"));
        for (String chunk : chunks) {
            int count = StringUtils.countMatches(message, chunk);
            if (count > 7) {
                command.message.delete();
                command.guild.config.addOffence(command.user.longID);
                logger.debug(Utility.loggingFormatter(command, "CATCH_SPAM_WALLS", "OFFENCE_ADDED", count + " Occurrences"));
                int offenceCount = command.guild.config.getOffender(command.user.longID).getCount();
                if (offenceCount > 2) {
                    if (command.guild.config.muteRepeatOffenders) {
                        Utility.muteUser(command, true);
                        command.user.sendDm("You were muted for spamming.");
                        logger.debug(Utility.loggingFormatter(command, "CATCH_SPAM_WALLS", "MUTE", offenceCount + " Offences"));
                        IChannel admin = command.guild.getChannelByType(Command.TYPE_ADMIN);
                        String report = command.user.mention() + " was muted for spamming";
                        if (admin != null) {
                            Utility.sendMessage(report + " in " + command.channel.get().mention() + ".", admin);
                        } else {
                            Utility.sendMessage(report + ".", command.channel.get());
                        }
                        return true;
                    }
                }
                command.user.sendDm("Your message was deleted because it was considered spam.");
                return true;
            }
        }
        return false;
    }

    public static boolean checkMentionCount(CommandObject command) {
        IMessage message = command.message.get();
        GuildConfig guildconfig = command.guild.config;
        IUser author = command.user.get();
        List<IRole> oldRoles = command.user.roles;
        IGuild guild = command.guild.get();

        if (message.toString().contains("@everyone") || message.toString().contains("@here")) {
            return false;
        }
        if (guildconfig.maxMentions) {
            if (message.getMentions().size() > 8) {
                Utility.deleteMessage(message);
                int i = 0;
                boolean offenderFound = false;
                for (OffenderObject o : guildconfig.getOffenders()) {
                    if (author.getLongID() == o.getID()) {
                        guildconfig.addOffence(o.getID());
                        logger.debug(Utility.loggingFormatter(command, "STOP_MASS_MENTIONS", "OFFENCE_ADDED", message.getMentions().size() + " Mentions"));
                        offenderFound = true;
                        i++;
                        if (o.getCount() >= Globals.maxWarnings) {
                            Utility.roleManagement(author, guild, guildconfig.getMutedRoleID(), true);
                            logger.debug(Utility.loggingFormatter(command, "STOP_MASS_MENTIONS", "MUTE", o.getCount() + " Offences"));
                            command.client.get().getDispatcher().dispatch(new UserRoleUpdateEvent(guild, author, oldRoles, command.user.get().getRolesForGuild(guild)));
                            Utility.sendMessage("> " + author.mention() + " Has been Muted for repeat offences of spamming Mentions.", command.channel.get());
                        }
                    }
                }
                if (!offenderFound) {
                    guildconfig.addOffender(new OffenderObject(author.getLongID()));
                }
                String response = "> #mentionAdmin# " + author.mention() + "  has attempted to post more than " + guildconfig.getMaxMentionLimit() + " Mentions in a single message.";
                IRole roleToMention = command.guild.getRoleByID(guildconfig.getRoleToMentionID());
                if (roleToMention != null) {
                    response = response.replaceAll("#mentionAdmin#", roleToMention.mention());
                } else {
                    response = response.replaceAll("#mentionAdmin#", "Admin");
                }
                Utility.sendMessage(response, command.channel.get());
                return true;
            }
        }
        return false;
    }


    public static boolean rateLimiting(CommandObject command) {
        //make sure that the rate limiting should actually happen
        if (Utility.testForPerms(command, Permissions.MANAGE_MESSAGES)) return false;
        if (!command.guild.config.rateLimiting) return false;
        if (!command.guild.rateLimit(command.user.longID)) return false;

        //send a dm to let the user know that they are being rate limited
        IChannel userDMs = command.user.get().getOrCreatePMChannel();
        if (userDMs != null) {
            Utility.sendMessage("Your message was deleted because you are being rate limited.\nMax messages per 10 seconds : " + command.guild.config.messageLimit, userDMs);
        }
        String contents = command.message.getContent();
        for (IMessage.Attachment a : command.message.getAttachments()) {
            contents += " <" + a.getUrl() + ">";
        }
        if (command.message.getContent() == null || command.message.getContent().isEmpty()) {
            contents = contents.replace("  ", "");
        }
        logger.debug(Utility.loggingFormatter(command, "RATE_LIMITING", "MESSAGE_DELETED", contents));

        //user is now being rate limited
        Utility.deleteMessage(command.message.get());
        if (command.guild.config.deleteLogging) {
            Utility.sendLog("> **@" + command.user.username + "** is being rate limited", command.guild, false);
        }

        //if mute continue
        if (!command.guild.config.muteRepeatOffenders) return true;
        IRole muteRole = command.guild.getRoleByID(command.guild.config.getMutedRoleID());
        if (muteRole == null) return true;

        //if they are over the rate limit start muting
        int rate = command.guild.getUserRate(command.user.longID);
        if (rate - 3 < command.guild.config.messageLimit) return true;

        logger.debug(Utility.loggingFormatter(command, "RATE_LIMITING", "MUTE", rate - 3 + " Over Rate"));

        //this mutes them
        RequestBuffer.request(() -> command.user.get().addRole(muteRole));

        //setup of the admin channel
        IChannel adminChannel = null;
        List<IChannel> adminChannels = command.guild.config.getChannelsByType(Command.CHANNEL_ADMIN, command.guild);
        if (adminChannels.size() != 0) adminChannel = adminChannels.get(0);
        if (adminChannel == null) adminChannel = command.channel.get();

        //sends the response if they got muted
        Utility.sendDM("You have been muted for abusing the Guild rate limit.", command.user.longID);
        Utility.sendMessage("> " + command.user.get().mention() + " has been muted for repetitively abusing Guild rateLimit.", adminChannel);
        return true;
    }

    public static boolean checkForInvites(CommandObject command) {
        GuildConfig guildconfig = command.guild.config;
        IMessage message = command.message.get();
        IGuild guild = command.guild.get();
        IUser author = command.user.get();
        List<String> inviteformats = new ArrayList<String>() {{
            add("discord.gg");
            add("discordapp.com/Invite/");
        }};

        boolean inviteFound = false;
        if (guildconfig.denyInvites) {
            for (String s : inviteformats) {
                if (message.toString().toLowerCase().contains(s.toLowerCase())) {
                    if (guildconfig.testIsTrusted(author, guild)) {
                        return false;
                    }
                    inviteFound = true;
                }
            }
        }
        if (inviteFound) {
            String response = "> Please do not post Instant Invites.";
            Utility.deleteMessage(message);
            logger.debug(Utility.loggingFormatter(command, "INVITE_REMOVAL", "REMOVED", message.getContent()));
            Utility.sendMessage(response, command.channel.get());
        }
        return inviteFound;
    }
}
