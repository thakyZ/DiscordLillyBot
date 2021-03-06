package com.github.vaerys.commands.creator.directmessages;

import com.github.vaerys.enums.SAILType;
import com.github.vaerys.handlers.RequestHandler;
import com.github.vaerys.main.Globals;
import com.github.vaerys.main.Utility;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.masterobjects.GlobalUserObject;
import com.github.vaerys.masterobjects.GuildObject;
import com.github.vaerys.templates.DMCommand;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Vaerys on 15/07/2017.
 */
public class TestDM extends DMCommand {

    @Override
    public String execute(String args, CommandObject command) {
//        GlobalUserObject user = new GlobalUserObject(command.user.longID);
//        Map<IUser, List<GuildObject>> mutuals = new HashMap<>();
//        GuildObject sandFox = Globals.getGuildContent(372416523861622787L);
//        IMessage working = RequestHandler.sendMessage("`Working...`", command.channel).get();
//        for (IUser u : sandFox.getUsers()) {
//            List<GuildObject> userMutuals = new LinkedList<>();
//            for (GuildObject g : user.guilds) {
//                if (g.getUserByID(u.getLongID()) != null) {
//                    userMutuals.add(g);
//                }
//            }
//            mutuals.put(u, userMutuals);
//        }
//        RequestHandler.deleteMessage(working);
//        for (Map.Entry<IUser, List<GuildObject>> e : mutuals.entrySet()) {
//            if (e.getValue().size() > 4) {
//                IUser mutualUser = e.getKey();
//                String mutualGuilds = Utility.listFormatter(e.getValue().stream().map(g -> g.get().getName()).collect(Collectors.toList()), true);
//                String response = String.format("%s#%s - Mutual Guilds: %d\n```\n%s```", mutualUser.getName(), mutualUser.getDiscriminator(), e.getValue().size(), mutualGuilds);
//                RequestHandler.sendMessage(response, command.channel).get();
//            }
//        }
        return "> Nothing to test right now.";
    }

    @Override
    protected String[] names() {
        return new String[]{"Test"};
    }

    @Override
    public String description(CommandObject command) {
        return "Is a test";
    }

    @Override
    protected String usage() {
        return "[args]";
    }

    @Override
    protected SAILType type() {
        return SAILType.CREATOR;
    }

    @Override
    protected boolean requiresArgs() {
        return true;
    }

    @Override
    public void init() {

    }
}
