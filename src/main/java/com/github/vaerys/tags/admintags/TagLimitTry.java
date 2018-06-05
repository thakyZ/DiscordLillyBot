package com.github.vaerys.tags.admintags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.AdminCCObject;
import com.github.vaerys.templates.TagAdminObject;

public class TagLimitTry extends TagAdminObject {

    public TagLimitTry(int priority, TagType... types) {
        super(priority, types);
    }

    @Override
    public String execute(String from, CommandObject command, String args, AdminCCObject cc) {
        if (command.guild.adminCCs.checkAttempt(command, cc)) {
            return contents(from);
        } else {
            return removeFirstTag(from);
        }
    }

    @Override
    protected String tagName() {
        return "limitTry";
    }

    @Override
    protected int argsRequired() {
        return 1;
    }

    @Override
    protected String usage() {
        return "Contents";
    }

    @Override
    protected String desc() {
        return "When activated if the user has been added to the Try list for this command it will return with the contents of the tag and close the admin cc process, else it will remove itself.";
    }
}
