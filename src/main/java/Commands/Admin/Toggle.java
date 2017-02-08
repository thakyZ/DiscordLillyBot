package Commands.Admin;

import Annotations.ToggleAnnotation;
import Commands.Command;
import Commands.CommandObject;
import Main.Utility;
import POGOs.GuildConfig;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Vaerys on 31/01/2017.
 */
public class Toggle implements Command {
    @Override
    public String execute(String args, CommandObject command) {
        StringBuilder builder = new StringBuilder();
        if (!args.isEmpty()) {
            Method[] methods = GuildConfig.class.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(ToggleAnnotation.class)) {
                    ToggleAnnotation toggleAnno = m.getAnnotation(ToggleAnnotation.class);
                    if (args.equalsIgnoreCase(toggleAnno.name())) {
                        try {
                            Boolean state = (Boolean) m.invoke(command.guildConfig);
                            return "> **" + toggleAnno.name() + " is now " + state + "**.";
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            builder.append("> Could not find toggle \"" + args + "\".\n");
        }
        Method[] methods = GuildConfig.class.getMethods();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String title = "> Here is a list of available Guild Toggles:\n";
        ArrayList<String> types = new ArrayList<>();
        for (Method m : methods) {
            if (m.isAnnotationPresent(ToggleAnnotation.class)) {
                ToggleAnnotation toggleAnno = m.getAnnotation(ToggleAnnotation.class);
                types.add(toggleAnno.name());
            }
        }
        Collections.sort(types);
        embedBuilder.withDesc(builder.toString());
        Utility.listFormatterEmbed(title,embedBuilder,types,true);
        embedBuilder.appendField(spacer,Utility.getCommandInfo(this,command),false);
        embedBuilder.withColor(Utility.getUsersColour(command.client.getOurUser(), command.guild));
        Utility.sendEmbededMessage("", embedBuilder.build(), command.channel);
        return null;
    }

    @Override
    public String[] names() {
        return new String[]{"Toggle"};
    }

    @Override
    public String description() {
        return "Toggles Certain Parts of the Guild Config.";
    }

    @Override
    public String usage() {
        return "(Toggle Type)";
    }

    @Override
    public String type() {
        return TYPE_ADMIN;
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
