package com.github.vaerys.commands.pixels;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.handlers.XpHandler;
import com.github.vaerys.interfaces.Command;
import com.github.vaerys.main.Utility;
import com.github.vaerys.masterobjects.UserObject;
import com.github.vaerys.objects.ProfileObject;
import com.github.vaerys.objects.SplitFirstObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 06/07/2017.
 */
public class SetLevel implements Command {
    @Override
    public String execute(String args, CommandObject command) {
        SplitFirstObject xpArgs = new SplitFirstObject(args);
        UserObject user = Utility.getUser(command, xpArgs.getFirstWord(), false);
        if (user == null) {
            return "> Could not find user.";
        }
        try {
            long level = Long.parseLong(xpArgs.getRest());
            long xp = XpHandler.totalXPForLevel(level);
            ProfileObject userObject = user.getProfile(command.guild);
            if (userObject != null) {
                userObject.setXp(xp);
                userObject.removeLevelFloor();
                XpHandler.checkUsersRoles(user.stringID, command.guild);
                return "> " + user.displayName + "'s Level is now set to: **" + level + "**";
            } else {
                return "> User does not have a profile.";
            }
        } catch (NumberFormatException e) {
            return "> Invalid number";
        }
    }

    @Override
    public String[] names() {
        return new String[]{"SetLevel"};
    }

    @Override
    public String description() {
        return "Allows you to set the level of a user.";
    }

    @Override
    public String usage() {
        return "[@User] [Level]";
    }

    @Override
    public String type() {
        return TYPE_PIXEL;
    }

    @Override
    public String channel() {
        return null;
    }

    @Override
    public Permissions[] perms() {
        return new Permissions[]{Permissions.MANAGE_SERVER};
    }

    @Override
    public boolean requiresArgs() {
        return true;
    }

    @Override
    public boolean doAdminLogging() {
        return true;
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