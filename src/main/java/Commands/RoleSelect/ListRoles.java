package Commands.RoleSelect;

import Commands.CommandObject;
import Interfaces.Command;
import Main.Utility;
import Objects.XEmbedBuilder;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;

/**
 * Created by Vaerys on 31/01/2017.
 */
public class ListRoles implements Command {

    @Override
    public String execute(String args, CommandObject command) {
        String title = "> Here are the **Cosmetic** roles you can choose from:\n";
        ArrayList<String> list = new ArrayList<>();
        XEmbedBuilder builder = new XEmbedBuilder();
        for (long l : command.guildConfig.getCosmeticRoleIDs()) {
            IRole role = command.guild.getRoleByID(l);
            list.add(role.getName());
        }
        Utility.listFormatterEmbed(title, builder, list, true);
        builder.appendField(spacer, Utility.getCommandInfo(new CosmeticRoles(), command), false);
        builder.withColor(Utility.getUsersColour(command.client.getOurUser(), command.guild));
        Utility.sendEmbedMessage("", builder, command.channel);
        return null;
    }

    @Override
    public String[] names() {
        return new String[]{"ListRoles", "Roles", "RoleList"};
    }

    @Override
    public String description() {
        return "Shows the list of cosmetic roles you can choose from.";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String type() {
        return TYPE_ROLE_SELECT;
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
