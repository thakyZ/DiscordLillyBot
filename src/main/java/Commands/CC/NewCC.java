package Commands.CC;

import Commands.Command;
import Commands.CommandObject;
import Main.TagSystem;
import Objects.SplitFirstObject;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 01/02/2017.
 */
public class NewCC implements Command{
    @Override
    public String execute(String args, CommandObject command) {
        boolean isShitpost = false;
        boolean isLocked = false;
        SplitFirstObject splitfirst = new SplitFirstObject(args);
        String newContent;
        if (command.channel.getID().equals(command.guildConfig.getChannelTypeID(Command.CHANNEL_SHITPOST))) {
            isShitpost = true;
        }
        String nameCC = splitfirst.getFirstWord();
        if (splitfirst.getRest() == null || splitfirst.getRest().isEmpty()) {
            return "> Custom command contents cannot be blank";
        }
        if (nameCC.contains("\n")) {
            return "> Command name cannot contain Newlines";
        }
        String content = splitfirst.getRest();
        newContent = TagSystem.testForShit(content);
        if (!newContent.equals(content)) {
            isShitpost = true;
        }
        content = newContent;
        newContent = TagSystem.testForLock(content, command.author, command.guild);
        if (!newContent.equals(content)) {
            isLocked = true;
        }
        content = newContent;
        return command.customCommands.addCommand(isLocked, command.author, nameCC, content, isShitpost, command.guild, command.guildConfig);
    }

    @Override
    public String[] names() {
        return new String[]{"NewCC"};
    }

    @Override
    public String description() {
        return "Creates a Custom Command.";
    }

    @Override
    public String usage() {
        return "[Command Name] [Contents]";
    }

    @Override
    public String type() {
        return TYPE_CC;
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
