package com.github.vaerys.commands.help;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.handlers.RequestHandler;
import com.github.vaerys.main.Globals;
import com.github.vaerys.main.Utility;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

/**
 * Created by Vaerys on 29/01/2017.
 */
public class Info extends Command {

    @Override
    public String execute(String args, CommandObject command) {
        List<Command> commands = command.guild.getAllCommands(command);
        if (command.user.longID == command.client.creator.longID) {
            commands.addAll(Globals.getCreatorCommands(false));
        }

        String error = "> Could not find information on any commands named **" + args + "**.";
        for (Command c : commands) {
            for (String s : c.names()) {
                if (args.equalsIgnoreCase(s) || args.equalsIgnoreCase(command.guild.config.getPrefixCommand() + s)) {
                    if (!Utility.testForPerms(command, c.perms())) {
                        return error;
                    }
                    RequestHandler.sendEmbedMessage("", c.getCommandInfo(command), command.channel.get());
                    return "";
                }
            }

        }
        return error;
    }

    @Override
    public String[] names() {
        return new String[]{"Help"};
    }

    @Override
    public String description(CommandObject command) {
        return "Gives information about a command.";
    }

    @Override
    public String usage() {
        return "[Command Name]";
    }

    @Override
    public String type() {
        return TYPE_HELP;
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
