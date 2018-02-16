package com.github.vaerys.commands.pixels;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.main.Utility;
import com.github.vaerys.tags.TagList;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 02/07/2017.
 */
public class LevelUpMessage extends Command {
    @Override
    public String execute(String args, CommandObject command) {
        if (args.length() > 100) {
            return "> Message to long.";
        } else {
            if (!args.contains("<level>") || !args.contains("<user>")) {
                return "> Missing <level> and/or <user> tags.";
            }
            command.guild.config.setLevelUpMessage(args);
            return "> New Level Up message set.";
        }
    }

    @Override
    public String[] names() {
        return new String[]{"SetLvlMessage","SetLevelMessage","SetLvlMsg","SetLevelMsg"};
    }

    @Override
    public String description(CommandObject command) {
        return "Allows you to set the level up message for the server.\n" +
                "**Tags:** " + Utility.listFormatter(TagList.getNames(TagList.LEVEL), true);
    }

    @Override
    public String usage() {
        return "[Message]";
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
