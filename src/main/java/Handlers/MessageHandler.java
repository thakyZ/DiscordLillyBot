package Handlers;

import Commands.Command;
import Commands.CommandObject;
import Main.Globals;
import Main.TimedEvents;
import Main.Utility;
import Objects.BlackListObject;
import Objects.OffenderObject;
import POGOs.GuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;
import sx.blah.discord.handle.obj.*;

import java.util.ArrayList;
import java.util.List;


/**
 * This Class Handles all of the commands that the bot can run not incluting custom commands.
 */


@SuppressWarnings({"unused", "StringConcatenationInsideStringBufferAppend"})
public class MessageHandler {

    private FileHandler handler = new FileHandler();

    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(String command, String args, CommandObject commandObject) {
        if (Globals.isModifingFiles) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        checkBlacklist(commandObject);
        checkMentionCount(commandObject);
        commandObject.guildUsers.addXP(commandObject);
        if (rateLimiting(commandObject)) {
            return;
        }

        if (commandObject.message.getAuthor().isBot()) {
            return;
        }
        if (command.toLowerCase().startsWith(commandObject.guildConfig.getPrefixCommand().toLowerCase())) {
            handleCommand(commandObject, command, args);
        }
        if (commandObject.guildConfig.moduleCC) {
            if (command.toLowerCase().startsWith(commandObject.guildConfig.getPrefixCC().toLowerCase())) {
                new CCHandler(command, args, commandObject);
            }
        }
    }

    //Command Handler
    private void handleCommand(CommandObject commandObject, String command, String args) {
        IChannel channel = commandObject.channel;
        GuildConfig guildConfig = commandObject.guildConfig;
        ArrayList<Command> commands = commandObject.commands;
        IDiscordClient client = commandObject.client;
        for (Command c : commands) {
            for (String name : c.names()) {
                if (command.equalsIgnoreCase(guildConfig.getPrefixCommand() + name)) {
                    //command logging

                    if (c.type().equals(Command.TYPE_CREATOR) && !commandObject.authorID.equalsIgnoreCase(Globals.creatorID)) {
                        return;
                    }

                    logger.debug(Utility.loggingFormatter("COMMAND", command, args, commandObject));

                    if (c.requiresArgs() && args.isEmpty()) {
                        Utility.sendMessage(Utility.getCommandInfo(c, commandObject), channel);
                        return;
                    }
                    if (c.channel() != null && !Utility.canBypass(commandObject.author, commandObject.guild)) {
                        IChannel correctChannel = client.getChannelByID(guildConfig.getChannelTypeID(c.channel()));
                        if (correctChannel != null) {
                            if (!channel.getID().equals(guildConfig.getChannelTypeID(c.channel()))) {
                                Utility.sendMessage("> Command must be performed in: " + correctChannel.mention(), channel);
                                return;
                            }
                        }
                    }
                    if (c.perms().length != 0 && !Utility.canBypass(commandObject.author, commandObject.guild)) {
                        if (!Utility.testForPerms(c.perms(), commandObject.author, commandObject.guild)) {
                            Utility.sendMessage(commandObject.notAllowed, channel);
                            return;
                        }
                    }
                    if (c.doAdminLogging()) {
                        if (guildConfig.adminLogging) {
                            IChannel logging = client.getChannelByID(guildConfig.getChannelTypeID(Command.CHANNEL_ADMIN_LOG));
                            handleLogging(logging, commandObject, args, c);
                        }
                    } else {
                        if (guildConfig.generalLogging) {
                            IChannel logging = client.getChannelByID(guildConfig.getChannelTypeID(Command.CHANNEL_SERVER_LOG));
                            handleLogging(logging, commandObject, args, c);
                        }
                    }
                    Utility.sendMessage(c.execute(args, commandObject), channel);
                }
            }
        }
    }

    private void handleLogging(IChannel loggingChannel, CommandObject commandObject, String args, Command command) {
        StringBuilder builder = new StringBuilder();
        builder.append("> **@" + commandObject.authorUserName + "** Has Used Command `" + command.names()[0] + "`");
        if (!args.isEmpty()) {
            builder.append(" with args: `" + args + "`");
        }
        builder.append(" in channel " + commandObject.channel.mention() + ".");
        Utility.sendMessage(builder.toString(), loggingChannel);
    }

    private void checkMentionCount(CommandObject command) {
        IMessage message = command.message;
        GuildConfig guildConfig = command.guildConfig;
        IUser author = command.author;
        List<IRole> oldRoles = new ArrayList<>(command.author.getRolesForGuild(command.guild));
        IGuild guild = command.guild;

        if (message.toString().contains("@everyone") || message.toString().contains("@here")) {
            return;
        }
        if (guildConfig.maxMentions) {
            if (message.getMentions().size() > 8) {
                Utility.deleteMessage(message);
                int i = 0;
                boolean offenderFound = false;
                for (OffenderObject o : guildConfig.getRepeatOffenders()) {
                    if (author.getID().equals(o.getID())) {
                        guildConfig.addOffence(o.getID());
                        offenderFound = true;
                        i++;
                        if (o.getCount() > Globals.maxWarnings) {
                            Utility.roleManagement(author, guild, guildConfig.getMutedRole().getRoleID(), true);
                            command.client.getDispatcher().dispatch(new UserRoleUpdateEvent(guild,author,oldRoles,command.author.getRolesForGuild(guild)));
                            Utility.sendMessage("> " + author.mention() + " Has been Muted for repeat offences of spamming Mentions.", command.channel);
                        }
                    }
                }
                if (!offenderFound) {
                    guildConfig.addOffender(new OffenderObject(author.getID()));
                }
                String response = "> #mentionAdmin# " + author.mention() + "  has attempted to post more than " + guildConfig.getMaxMentionLimit() + " Mentions in a single message.";
                if (guildConfig.getRoleToMention().getRoleID() != null) {
                    response = response.replaceAll("#mentionAdmin#", guild.getRoleByID(guildConfig.getRoleToMention().getRoleID()).mention());
                } else {
                    response = response.replaceAll("#mentionAdmin#", "Admin");
                }
                if (TimedEvents.getDoAdminMention(command.guildID) == 0) {
                    Utility.sendMessage(response, command.channel);
                    TimedEvents.setDoAdminMention(command.guildID, 60);
                }
            }
        }
    }

    private boolean rateLimiting(CommandObject command) {
        if (Utility.testForPerms(new Permissions[]{Permissions.MANAGE_MESSAGES}, command.author, command.guild, false) ||
                Utility.canBypass(command.author, command.guild, false)) {
            return false;
        }
        if (command.guildConfig.rateLimiting) {
            if (command.guildContent.rateLimit(command.authorID)) {
                List<IRole> oldRoles = new ArrayList<>(command.author.getRolesForGuild(command.guild));
                command.message.delete();
                Utility.sendDM("Your message was deleted because you are being rate limited.\nMax messages per 10 seconds : " + command.guildConfig.messageLimit, command.authorID);
                if (command.guildConfig.muteRepeatOffenders) {
                    int rate = command.guildContent.getUserRate(command.authorID);
                    if (rate - 3 > command.guildConfig.messageLimit) {
                        //mutes users if they abuse it.
                        boolean failed = Utility.roleManagement(command.author, command.guild, command.guildConfig.getMutedRole().getRoleID(), true).get();
                        command.client.getDispatcher().dispatch(new UserRoleUpdateEvent(command.guild,command.author,oldRoles,command.author.getRolesForGuild(command.guild)));
                        if (!failed) {
                            IChannel adminChannel = command.client.getChannelByID(command.guildConfig.getChannelTypeID(Command.CHANNEL_ADMIN));
                            if (adminChannel == null){
                                adminChannel = command.channel;
                            }
                            Utility.sendDM("You have been muted for abusing the Guild rate limit.", command.authorID);
                            Utility.sendMessage("> " + command.author.mention() + " has been muted for repetitively abusing Guild rateLimit.", adminChannel);
                        }
                    }
                }
                if (command.guildConfig.deleteLogging) {
                    if (command.guildConfig.getChannelTypeID(Command.CHANNEL_SERVER_LOG) != null) {
                        IChannel logging = command.client.getChannelByID(command.guildConfig.getChannelTypeID(Command.CHANNEL_SERVER_LOG));
                        Utility.sendMessage("> **@" + command.authorUserName + "** is being rate limited", logging);
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //File handlers

    //BlackListed Phrase Remover
    private void checkBlacklist(CommandObject command) {
        GuildConfig guildConfig = command.guildConfig;
        IMessage message = command.message;
        IGuild guild = command.guild;
        IUser author = command.author;

        if (guildConfig == null) {
            return;
        }
        if (guildConfig.getBlackList() == null) {
            return;
        }
        if (guildConfig.denyInvites) {
            for (BlackListObject bLP : guildConfig.getBlackList()) {
                if (message.toString().toLowerCase().contains(bLP.getPhrase().toLowerCase())) {
                    if (guildConfig.testIsTrusted(author, guild)) {
                        return;
                    }
                    String response = bLP.getReason();
                    if (response.contains("#mentionAdmin#")) {
                        if (guildConfig.getRoleToMention().getRoleID() != null) {
                            response = response.replaceAll("#mentionAdmin#", guild.getRoleByID(guildConfig.getRoleToMention().getRoleID()).mention());
                        } else {
                            response = response.replaceAll("#mentionAdmin#", "Admin");
                        }
                        if (TimedEvents.getDoAdminMention(command.guildID) == 0) {
                            Utility.sendMessage(response, command.channel);
                            TimedEvents.setDoAdminMention(command.guildID, 60);
                        }
                        Utility.deleteMessage(message);
                    } else {
                        Utility.deleteMessage(message);
                        Utility.sendMessage(response, command.channel);
                    }
                }
            }
        }
    }
}