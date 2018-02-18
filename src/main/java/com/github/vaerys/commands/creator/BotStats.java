package com.github.vaerys.commands.creator;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.handlers.StringHandler;
import com.github.vaerys.handlers.TimerHandler;
import com.github.vaerys.objects.XEmbedBuilder;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.Permissions;

import java.text.NumberFormat;
import java.util.List;

public class BotStats extends Command {

    @Override
    public String execute(String args, CommandObject command) {
        List<Double> cpuUsage = TimerHandler.cpuUsage;
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;
        double usage = cpuUsage.stream().mapToDouble(value -> value).sum() / cpuUsage.size();
        // and make it look pretty
        NumberFormat nf = NumberFormat.getInstance();
        long ping = command.client.get().getShards().get(0).getResponseTime();
        double mb = 1048576.0;

        XEmbedBuilder builder = new XEmbedBuilder(command);
        builder.withTitle(command.client.bot.username);
        builder.withTimestamp(command.client.bot.get().getCreationDate());
        builder.withFooterText("Creation Date");
        StringHandler handler = new StringHandler();
        handler.append("**Total Servers**: ").append(command.client.get().getGuilds().size());
        handler.append("\n**Total Active Threads**: ").append(Thread.activeCount());
        handler.append("\n**CPU Usage**: ").append(nf.format(usage * 100)).append("%");
        handler.append("\n**Memory Usage**: ");
        nf.setMaximumFractionDigits(1);
        handler.append(nf.format(totalMemory / mb)).append("MB total\t");
        handler.append(nf.format(usedMemory / mb)).append("MB used\t");
        handler.append(nf.format(freeMemory / mb)).append("MB free");
        handler.append("\n**Ping**: ").append(nf.format(ping)).append("ms");
        builder.withDesc(handler.toString());
        builder.withThumbnail(command.client.bot.getAvatarURL());
        builder.send(command.channel);
        return null;
    }

    @Override
    public String[] names() {
        return new String[]{"BotStats"};
    }

    @Override
    public String description(CommandObject command) {
        return "Gives the General stats of the bot.";
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