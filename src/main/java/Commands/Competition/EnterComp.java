package Commands.Competition;

import Commands.Command;
import Commands.CommandObject;
import Objects.PollObject;
import POGOs.GuildConfig;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vaerys on 01/02/2017.
 */
public class EnterComp implements Command {
    @Override
    public String execute(String args, CommandObject command) {
        GuildConfig guildConfig = command.guildConfig;
        IMessage message = command.message;
        IUser author = command.author;

        if (guildConfig.compEntries) {
            String fileName;
            String fileUrl;
            if (message.getAttachments().size() > 0) {
                List<IMessage.Attachment> attatchments = message.getAttachments();
                IMessage.Attachment a = attatchments.get(0);
                fileName = a.getFilename();
                fileUrl = a.getUrl();
            } else if (!args.isEmpty()) {
                fileName = author.getName() + "'s Entry";
                fileUrl = args;
            } else {
                return "> Missing a File or Image link to enter into the competition.";
            }
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            command.competition.newEntry(new PollObject(author.getDisplayName(command.guild), author.getID(), fileName, fileUrl, dateFormat.format(cal.getTime())));
            return "> Thank you " + author.getDisplayName(command.guild) + " For entering the Competition.";
        } else {
            return "> Competition Entries are closed.";
        }
    }

    @Override
    public String[] names() {
        return new String[]{"EnterComp","Comp","Enter"};
    }

    @Override
    public String description() {
        return "Enters your image into the Sail Competition.";
    }

    @Override
    public String usage() {
        return "[Image Link or Image File]";
    }

    @Override
    public String type() {
        return TYPE_COMPETITION;
    }

    @Override
    public String channel() {
        return CHANNEL_BOT_COMMANDS;
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
