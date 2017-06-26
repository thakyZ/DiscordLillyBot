package Commands.General;

import Commands.CommandObject;
import Interfaces.Command;
import Main.Utility;
import Objects.SplitFirstObject;
import Objects.UserTypeObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 27/02/2017.
 */
public class SetQuote implements Command {

    @Override
    public String execute(String args, CommandObject command) {
        IUser user = command.author;
        SplitFirstObject userID = new SplitFirstObject(args);
        boolean adminEdit = false;
        if (Utility.testForPerms(dualPerms(), command.author, command.guild) || Utility.canBypass(command.author, command.guild)) {
            try {
                user = command.client.getUserByID(Long.parseLong(userID.getFirstWord()));
                if (user != null) {
                    adminEdit = true;
                }
            }catch (NumberFormatException e){
                //do nothing
            }
        }
        for (UserTypeObject u : command.guildUsers.getUsers()) {
            args = Utility.removeFun(args);
            for (String s: args.split(" ")){
                if (!Utility.checkURL(s)){
                    return "> Cannot add quote. Malicious link found.";
                }
            }
            if (adminEdit) {
                if (userID.getRest().length() > 140){
                    return "> User Quote is too long...\n(must be under 140 chars)";
                }
                if (u.getID().equals(user.getStringID())) {
                    u.setQuote(userID.getRest());
                    return "> User's Quote Edited.";
                }
            }else {
                if (args.length() > 140) {
                    return "> Your Quote is too long...\n(must be under 140 chars)";
                }
                if (u.getID().equals(command.authorSID)) {
                    u.setQuote(args);
                    return "> Quote Edited.";
                }
            }
        }
        return "> User Has not Spoken yet thus they have nothing to edit.";
    }

    @Override
    public String[] names() {
        return new String[]{"SetQuote"};
    }

    @Override
    public String description() {
        return "Allows you to set your quote. Limit 140 chars.";
    }

    @Override
    public String usage() {
        return "[Quote...]";
    }

    @Override
    public String type() {
        return TYPE_GENERAL;
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
        return true;
    }

    @Override
    public boolean doAdminLogging() {
        return false;
    }

    @Override
    public String dualDescription() {
        return "Allows you to set the quote of a user.";
    }

    @Override
    public String dualUsage() {
        return "[UserID] [User Quote]";
    }

    @Override
    public String dualType() {
        return TYPE_ADMIN;
    }

    @Override
    public Permissions[] dualPerms() {
        return new Permissions[]{Permissions.MANAGE_MESSAGES};
    }
}
