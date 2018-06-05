package com.github.vaerys.handlers;

import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.TagType;
import com.github.vaerys.enums.UserSetting;
import com.github.vaerys.main.Utility;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.AdminCCObject;
import com.github.vaerys.objects.CCommandObject;
import com.github.vaerys.objects.ProfileObject;
import com.github.vaerys.objects.SplitFirstObject;
import com.github.vaerys.tags.TagList;
import com.github.vaerys.templates.TagObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

/**
 * Created by Vaerys on 27/08/2016.
 */
public class CCHandler {

    private final static Logger logger = LoggerFactory.getLogger(CCHandler.class);

    public static void handleAdminCC(String args, CommandObject command) {

        SplitFirstObject commandName = new SplitFirstObject(args);
        AdminCCObject cc = command.guild.adminCCs.getCommand(commandName.getFirstWord(), command);

        if (cc == null) return;

        String ccArgs = commandName.getRest();
        if (ccArgs == null) {
            ccArgs = "";
        }

        List<IChannel> ccDenied = command.guild.getChannelsByType(ChannelSetting.CC_DENIED);
        if (ccDenied.contains(command.channel.get())) {
            RequestHandler.sendMessage("> Custom Command usage has been disabled for this channel.", command.channel);
            return;
        }

        ProfileObject object = command.guild.users.getUserByID(command.user.longID);
        if (object != null && object.getSettings().contains(UserSetting.DENY_USE_CCS)) {
            RequestHandler.sendMessage("> Nothing interesting happens. `(ERROR: 403)`", command.channel.get());
            return;
        }

        command.guild.sendDebugLog(command, "ADMIN_CUSTOM_COMMAND", cc.getName(command), ccArgs);

        String contents = cc.getContents(true);

        List<TagObject> tags = TagList.getType(TagType.CC);
        tags.addAll(TagList.getType(TagType.ADMIN_CC));
        TagList.sort(tags);

        for (TagObject t : tags) {
            contents = t.handleTag(contents, command, ccArgs, cc);
            if (contents == null) return;
        }
        cc.cullKeys();
        RequestHandler.sendMessage(contents, command.channel.get());
    }

    public static void handleCommand(String args, CommandObject command) {
        //cc lockout handling


        SplitFirstObject commandName = new SplitFirstObject(args);
        CCommandObject commandObject = command.guild.customCommands.getCommand(commandName.getFirstWord(), command);

        String ccArgs = commandName.getRest();
        if (ccArgs == null) {
            ccArgs = "";
        }

        if (commandObject == null) return;

        List<IChannel> ccDenied = command.guild.getChannelsByType(ChannelSetting.CC_DENIED);
        if (ccDenied.contains(command.channel.get())) {
            RequestHandler.sendMessage("> Custom Command usage has been disabled for this channel.", command.channel);
            return;
        }

        ProfileObject object = command.guild.users.getUserByID(command.user.longID);
        if (object != null && object.getSettings().contains(UserSetting.DENY_USE_CCS)) {
            RequestHandler.sendMessage("> Nothing interesting happens. `(ERROR: 403)`", command.channel.get());
            return;
        }

        command.guild.sendDebugLog(command, "CUSTOM_COMMAND", commandObject.getName(command), ccArgs);

        String contents = commandObject.getContents(true);
        //shitpost handling
        if (commandObject.isShitPost() && command.guild.config.shitPostFiltering && !GuildHandler.testForPerms(command, Permissions.MANAGE_CHANNELS)) {
            List<IChannel> channels = command.guild.getChannelsByType(ChannelSetting.SHITPOST);
            if (channels.size() != 0 && !channels.contains(command.channel.get())) {
                channels = command.user.getVisibleChannels(channels);
                List<String> channelMentions = Utility.getChannelMentions(channels);
                RequestHandler.sendMessage(Utility.getChannelMessage(channelMentions), command.channel.get());
                return;
            }
        }

        //tag handling
        for (TagObject t : TagList.getType(TagType.CC)) {
            contents = t.handleTag(contents, command, ccArgs);
            if (contents == null) return;
        }
        RequestHandler.sendMessage(contents, command.channel.get());
    }
}
