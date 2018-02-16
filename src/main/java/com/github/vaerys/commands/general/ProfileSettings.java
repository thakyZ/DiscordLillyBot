package com.github.vaerys.commands.general;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.main.Constants;
import com.github.vaerys.main.UserSetting;
import com.github.vaerys.main.Utility;
import com.github.vaerys.objects.ProfileObject;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Vaerys on 02/07/2017.
 */
public class ProfileSettings extends Command {


    @Override
    public String execute(String args, CommandObject command) {
        String message = "> Your Level messages will now be sent to ";
        ProfileObject userObject = command.guild.users.getUserByID(command.user.longID);
        StringBuilder builder = new StringBuilder();
        boolean pixels = command.guild.config.modulePixels;
        boolean levelChannel = command.guild.getChannelsByType(CHANNEL_LEVEL_UP).size() != 0;
        String error = getSettings(command) + "\n" + Utility.getCommandInfo(this, command);

        for (String s : args.split(" ")) {
            UserSetting toTest = UserSetting.get(s);
            if (toTest == null) {
                return error;
            }
            if (pixels && levelChannel && toTest == UserSetting.SEND_LVLUP_RANK_CHANNEL) {
                IChannel levelUp = command.guild.getChannelByType(CHANNEL_LEVEL_UP);
                if (levelUp != null) {
                    removeLevelSettings(userObject);
                    userObject.getSettings().add(UserSetting.SEND_LVLUP_RANK_CHANNEL);
                    builder.append(message + levelUp.mention() + ".");
                } else {
                    removeLevelSettings(userObject);
                    userObject.getSettings().add(UserSetting.SEND_LVLUP_CURRENT_CHANNEL);
                    builder.append(message + "the current channel.");
                }
            } else if (pixels && toTest == UserSetting.SEND_LVLUP_CURRENT_CHANNEL) {
                removeLevelSettings(userObject);
                userObject.getSettings().add(UserSetting.SEND_LVLUP_CURRENT_CHANNEL);
                builder.append(message + "the current channel.");
            } else if (pixels && toTest == UserSetting.SEND_LVLUP_DMS) {
                removeLevelSettings(userObject);
                userObject.getSettings().add(UserSetting.SEND_LVLUP_DMS);
                builder.append(message + "your Direct messages.");
            } else if (pixels && toTest == UserSetting.DONT_SEND_LVLUP) {
                removeLevelSettings(userObject);
                userObject.getSettings().add(UserSetting.DONT_SEND_LVLUP);
                builder.append("> You will no longer see any level up messages.");
            } else if (pixels && toTest == UserSetting.NO_XP_GAIN) {
                builder.append(toggleSetting(userObject, UserSetting.NO_XP_GAIN,
                        "> You will now gain Xp again.",
                        "> You will no longer gain XP"));
            } else if (pixels && toTest == UserSetting.HIDE_RANK) {
                builder.append(toggleSetting(userObject, UserSetting.HIDE_RANK,
                        "> Your rank is now visible.",
                        "> Your rank is now hidden."));
            } else if (pixels && toTest == UserSetting.NO_LEVEL_UP_REACTIONS) {
                builder.append(toggleSetting(userObject, UserSetting.NO_LEVEL_UP_REACTIONS,
                        "> You will now get reactions added to the message that leveled you up.",
                        "> You will no longer get reactions added to the message that leveled you up."));
            } else if (toTest == UserSetting.PRIVATE_PROFILE) {
                builder.append(toggleSetting(userObject, UserSetting.PRIVATE_PROFILE,
                        "> Your profile is now public.",
                        "> Your profile is now private."));
            } else {
                if (!builder.toString().contains(error)) {
                    builder.append(error);
                }
            }
            if (!builder.toString().endsWith("\n")) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private String getSettings(CommandObject object) {
        StringBuilder builder = new StringBuilder();
        List<IChannel> levelUp = object.guild.getChannelsByType(CHANNEL_LEVEL_UP);
        builder.append("**Settings:**\n");
        if (object.guild.config.modulePixels) {
            if (levelUp.size() != 0) {
                builder.append("> LevelChannel - `Level up messages will be sent to `" + levelUp.get(0).mention() + "\n");
            }
            builder.append("> CurrentChannel - `Level up messages will be sent to the current channel.`\n" +
                    "> DMs - `Level up messages will be sent to your DMs.`\n" +
                    "> NoLevelMessages - `Hides your Level up messages.`\n" +
                    "> NoXP - `Stops you from gaining pixels.`\n" +
                    "> HideRank - `Hides your rank on the server.`\n" +
                    "> NoLevelUpReactions - `Stops level up reactions.`\n");
        }
        builder.append("> PrivateProfile - `Hides your profile from other users.`");
        if (object.guild.config.modulePixels) {
            UserSetting setting = object.guild.config.defaultLevelMode;
            if (setting == UserSetting.SEND_LVLUP_RANK_CHANNEL && levelUp.size() == 0) {
                setting = UserSetting.SEND_LVLUP_CURRENT_CHANNEL;
            }
            builder.append("\n\nCurrent Guild Default: **" + UserSetting.get(setting) + "**\n");
        } else {
            builder.append("\n");
        }
        return builder.toString();
    }

    private String toggleSetting(ProfileObject user, UserSetting setting, String remove, String add) {
        if (user.getSettings().contains(setting)) {
            ListIterator iterator = user.getSettings().listIterator();
            while (iterator.hasNext()) {
                UserSetting s = (UserSetting) iterator.next();
                if (s == setting) {
                    iterator.remove();
                }
            }
            return remove;
        } else {
            user.getSettings().add(setting);
            return add;
        }
    }

    private static void removeLevelSettings(ProfileObject user) {
        for (int i = 0; i < user.getSettings().size(); i++) {
            if (Constants.levelUpStates.contains(user.getSettings().get(i))) {
                user.getSettings().remove(i);
            }
        }
    }

    @Override
    public String[] names() {
        return new String[]{"ProfileSettings", "PixelSettings"};
    }

    @Override
    public String description(CommandObject command) {
        return "Allows you to set where your level up messages will be sent.\n" + getSettings(command);
    }

    @Override
    public String usage() {
        return "(Setting...)";
    }

    @Override
    public String type() {
        return TYPE_GENERAL;
    }

    @Override
    public String channel() {
        return null;
    }

    @Override
    public Permissions[] perms() {
        return new Permissions[0];
    }

    @Override
    public boolean requiresArgs() {
        return false;
    }

    @Override
    public boolean doAdminLogging() {
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public String dualDescription() {
        return null;
    }

    @Override
    public String dualUsage() {
        return null;
    }

    @Override
    public String dualType() {
        return null;
    }

    @Override
    public Permissions[] dualPerms() {
        return new Permissions[0];
    }
}
