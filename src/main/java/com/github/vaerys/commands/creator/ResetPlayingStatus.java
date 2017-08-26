package com.github.vaerys.commands.creator;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.interfaces.Command;
import com.github.vaerys.main.Globals;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 21/03/2017.
 */
public class ResetPlayingStatus implements Command{
    @Override
    public String execute(String args, CommandObject command) {
        command.client.get().changePlayingText(Globals.playing);
        return "> Status reset.";
    }

    @Override
    public String[] names() {
        return new String[]{"ResetPlayingStatus"};
    }

    @Override
    public String description() {
        return "Resets the playing status.";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String type() {
        return TYPE_CREATOR;
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
