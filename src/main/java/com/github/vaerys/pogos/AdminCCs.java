package com.github.vaerys.pogos;

import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.AdminCCObject;
import com.github.vaerys.objects.DualVar;
import com.github.vaerys.objects.TriVar;
import com.github.vaerys.templates.GlobalFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class AdminCCs extends GlobalFile {
    private double fileVersion = 1.0;
    private List<AdminCCObject> commands = new ArrayList<>(20);
    private List<DualVar<Long, String>> tries = new LinkedList<>();

    public List<AdminCCObject> getCommands() {
        return commands;
    }

    public List<DualVar<Long, String>> getTries() {
        return tries;
    }

    public DualVar<Long, String> addTry(long userID, String commandName) {
        DualVar<Long, String> newTry = new DualVar<>(userID, commandName);
        tries.add(newTry);
        return newTry;
    }

    public AdminCCObject addCommand(CommandObject command, String ccName, String contents) {
        AdminCCObject cc = new AdminCCObject(ccName, command.user.longID, contents);
        commands.add(cc);
        return cc;
    }

    public boolean checkAttempt(CommandObject command, AdminCCObject cc) {
        for (DualVar<Long, String> t : tries) {
            if (t.getVar1() == command.user.longID && t.getVar2().equalsIgnoreCase(cc.getName())) {
                return true;
            }
        }
        return false;
    }

    public AdminCCObject getCommand(String name, CommandObject command) {
        for (AdminCCObject c : commands) {
            if ((command.guild.config.getPrefixAdminCC() + c.getName()).equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public AdminCCObject getCommand(String name) {
        for (AdminCCObject c : commands) {
            if ((c.getName()).equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public void dailyReset() {
        for (AdminCCObject c : commands) {
            if (c.hasLimitTry()) {
                c.removeAllKeys();
            }
        }
        tries.clear();
    }

    public void purgeKeys() {
        Instant now = Instant.now();
        long fiveMinsMilli = 5 * 60 * 1000;
        for (AdminCCObject c : commands) {
            if (c.hasLimitTry()) continue;
            ListIterator iterator = c.getPathKeys().listIterator();
            while (iterator.hasNext()) {
                TriVar<Long, String, Long> key = (TriVar<Long, String, Long>) iterator.next();
                if ((now.toEpochMilli() - fiveMinsMilli) > key.getVar3()) {
                    iterator.remove();
                }
            }
        }
    }

    public boolean commandExists(String name) {
        for (AdminCCObject c : commands) {
            if (c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void removeCommand(String args) {
        for (AdminCCObject c: commands){
            if (c.getName().equalsIgnoreCase(args)){
                commands.remove(c);
                return;
            }
        }
    }
}
