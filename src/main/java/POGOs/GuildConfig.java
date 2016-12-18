package POGOs;

import Annotations.ToggleAnnotation;
import Main.Constants;
import Main.Globals;
import Objects.BlackListObject;
import Objects.ChannelTypeObject;
import Objects.OffenderObject;
import Objects.RoleTypeObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;

/**
 * Created by Vaerys on 03/08/2016.
 */
public class GuildConfig {
    String prefixCommand = Constants.PREFIX_COMMAND;
    String prefixCC = Constants.PREFIX_CC;
    boolean properlyInit = false;
    String guildName = "";
    boolean loginMessage = true;
    boolean generalLogging = false;
    boolean adminLogging = false;
    boolean blackListing = false;
    boolean maxMentions = true;
    boolean dailyMessage = true;
    boolean shitPostFiltering = false;
    boolean muteRepeatOffenders = true;
    boolean compEntries = false;
    boolean compVoting = false;
    int maxMentionLimit = 8;

    // TODO: 04/10/2016 let the mention limit be customisable.
    ArrayList<ChannelTypeObject> channels = new ArrayList<>();
    ArrayList<RoleTypeObject> cosmeticRoles = new ArrayList<>();
    ArrayList<RoleTypeObject> modifierRoles = new ArrayList<>();
    ArrayList<RoleTypeObject> trustedRoles = new ArrayList<>();
    ArrayList<BlackListObject> blackList = new ArrayList<>();
    ArrayList<OffenderObject> repeatOffenders = new ArrayList<>();
    RoleTypeObject roleToMention = new RoleTypeObject("No Role Set", null);
    RoleTypeObject mutedRole = new RoleTypeObject("No Role Set", null);

    public String getPrefixCommand() {
        return prefixCommand;
    }

    public void setPrefixCommand(String prefixCommand) {
        this.prefixCommand = prefixCommand;
    }

    public String getPrefixCC() {
        return prefixCC;
    }

    public void setPrefixCC(String prefixCC) {
        this.prefixCC = prefixCC;
    }

    public boolean isProperlyInit() {
        return properlyInit;
    }

    public void setProperlyInit(boolean properlyInit) {
        this.properlyInit = properlyInit;
    }

    public void initConfig() {
        if (!properlyInit) {
            blackList.add(new BlackListObject("discord.gg", "Please do not put **invites** in Custom Commands."));
            blackList.add(new BlackListObject("discordapp.com/Invite/", "Please do not put **invites** in Custom Commands."));
        }
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public ArrayList<ChannelTypeObject> getChannels() {
        return channels;
    }

    //Getters For the Toggles.
    public boolean doLoginMessage() {
        return loginMessage;
    }

    public int getMaxMentionLimit() {
        return maxMentionLimit;
    }

    public boolean doGeneralLogging() {
        return generalLogging;
    }

    public boolean doBlackListing() {
        return blackListing;
    }

    public boolean doShitPostFiltering() {
        return shitPostFiltering;
    }

    public boolean doAdminLogging() {
        return adminLogging;
    }

    public boolean doMaxMentions() {
        return maxMentions;
    }

    public boolean doDailyMessage() {
        return dailyMessage;
    }

    public boolean doMuteRepeatOffenders() {
        return muteRepeatOffenders;
    }

    public boolean doCompEntries() {
        return compEntries;
    }

    public boolean doCompVoting() {
        return compVoting;
    }

    //Togglers
    @ToggleAnnotation(name = "LoginMessage")
    public void toggleDoLoginMessage() {
        loginMessage = !loginMessage;
    }

    @ToggleAnnotation(name = "GeneralLogging")
    public void toggleLogging() {
        generalLogging = !generalLogging;
    }

    @ToggleAnnotation(name = "AdminLogging")
    public void toggleAdminLogging() {
        adminLogging = !adminLogging;
    }

    @ToggleAnnotation(name = "BlackListing")
    public void toggleDoBlackListing() {
        blackListing = !blackListing;
    }

    @ToggleAnnotation(name = "MentionSpam")
    public void toggelMaxMentions() {
        maxMentions = !maxMentions;
    }

    @ToggleAnnotation(name = "DailyMessage")
    public void toggleDailyMessage() {
        dailyMessage = !dailyMessage;
    }

    @ToggleAnnotation(name = "ShitPostFiltering")
    public void toggleShitPostFiltering() {
        shitPostFiltering = !shitPostFiltering;
    }

    @ToggleAnnotation(name = "MuteRepeatOffender")
    public void toggleRepeatOffender() {
        muteRepeatOffenders = !muteRepeatOffenders;
    }

    @ToggleAnnotation(name = "CompEntries")
    public void toggleCompEntries() {
        compEntries = !compEntries;
    }

    @ToggleAnnotation(name = "Voting")
    public void setCompVoting() {
        compVoting = !compVoting;
    }

    public void setUpChannel(String channelType, String channelID) {
        if (channels.size() == 0) {
            channels.add(new ChannelTypeObject(channelType, channelID));
            return;
        }
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getType().equals(channelType)) {
                channels.set(i, new ChannelTypeObject(channelType, channelID));
                return;
            }
        }
        channels.add(new ChannelTypeObject(channelType, channelID));
    }

    public String getChannelTypeID(String channelType) {
        for (ChannelTypeObject c : channels) {
            if (c.getType().equals(channelType)) {
                return c.getID();
            }
        }
        return null;
    }

    public void updateVariables(IGuild guild) {
        //update Guild Name
        setGuildName(guild.getName());

        //Update Races
        ArrayList<RoleTypeObject> newRaces = new ArrayList<>();
        for (RoleTypeObject r : cosmeticRoles) {
            if (guild.getRoleByID(r.getRoleID()) != null) {
                r.updateRoleName(guild.getRoleByID(r.getRoleID()).getName());
                newRaces.add(r);
            }
        }
        cosmeticRoles = newRaces;

        //Update Trusted Roles
        ArrayList<RoleTypeObject> newTrustedRoles = new ArrayList<>();
        for (RoleTypeObject r : trustedRoles) {
            if (guild.getRoleByID(r.getRoleID()) != null) {
                r.updateRoleName(guild.getRoleByID(r.getRoleID()).getName());
                newTrustedRoles.add(r);
            }
        }
        trustedRoles = newTrustedRoles;

        //Update Role to Mention
        if (roleToMention.getRoleID() != null) {
            if (guild.getRoleByID(roleToMention.getRoleID()) == null) {
                roleToMention = new RoleTypeObject("Role Missing", null);
            } else {
                roleToMention.updateRoleName(guild.getRoleByID(roleToMention.getRoleID()).getName());
            }
        }

        //update Muted Role
        if (mutedRole.getRoleID() != null) {
            if (guild.getRoleByID(mutedRole.getRoleID()) == null) {
                mutedRole = new RoleTypeObject("Role Missing", null);
            } else {
                mutedRole.updateRoleName(guild.getRoleByID(mutedRole.getRoleID()).getName());
            }
        }

        //update repeat offenders.
        ArrayList<OffenderObject> newMentionSpammers = new ArrayList<>();
        for (int i = 0; i < repeatOffenders.size(); i++) {
            IUser offender = Globals.getClient().getUserByID(repeatOffenders.get(i).getID());
            if (offender != null) {
                repeatOffenders.get(i).setDisplayName(offender.getName() + "#" + offender.getDiscriminator());
            }
        }
        repeatOffenders = newMentionSpammers;
    }

    public ArrayList<BlackListObject> getBlackList() {
        return blackList;
    }

    public void addBlacklistItem(String phrase, String reason) {
        blackList.add(new BlackListObject(phrase, reason));
    }

    public void removeBlackListItem(String phrase) {
        for (int i = 0; i < blackList.size(); i++) {
            if (blackList.get(1).getPhrase().equalsIgnoreCase(phrase)) {
                blackList.remove(i);
            }
        }
    }

    public String setRoleToMention(String roleName, String roleID) {
        roleToMention = new RoleTypeObject(roleName, roleID);
        return "> the Role `" + roleName + "` will now be mentioned when the tag #admin# is called withing the blacklisting process.";
    }

    public RoleTypeObject getRoleToMention() {
        return roleToMention;
    }

    public String addRole(String roleID, String roleName, boolean isCosmetic) {
        ArrayList<RoleTypeObject> roleList;
        boolean isfound = false;
        int i = 0;
        if (isCosmetic) {
            roleList = cosmeticRoles;
        } else {
            roleList = modifierRoles;
        }
        while (i < roleList.size()) {
            if (roleList.get(i).getRoleID().equals(roleID)) {
                isfound = true;
            }
            i++;
        }
        if (!isfound) {
            roleList.add(new RoleTypeObject(roleName, roleID));
            if (isCosmetic) {
                cosmeticRoles = roleList;
            } else {
                modifierRoles = roleList;
            }
            return "> Role `" + roleName + "` Added to Role List.";
        }
        return "> Role already added to list.";
    }

    public String removeRole(String roleID, String roleName, boolean isCosmetic) {
        ArrayList<RoleTypeObject> roleList;
        if (isCosmetic) {
            roleList = cosmeticRoles;
        } else {
            roleList = modifierRoles;
        }
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getRoleID().equals(roleID)) {
                roleList.remove(i);
                if (isCosmetic) {
                    cosmeticRoles = roleList;
                } else {
                    modifierRoles = roleList;
                }
                return "> Role `" + roleName + "` Removed from Role List.";
            }
        }
        return "> Role not in list of roles.";
    }

    public ArrayList<RoleTypeObject> getModifierRoles() {
        return modifierRoles;
    }

    public ArrayList<RoleTypeObject> getCosmeticRoles() {
        return cosmeticRoles;
    }

    public boolean testIsTrusted(IUser author, IGuild guild) {
        if (trustedRoles.size() == 0) {
            return true;
        } else {
            for (RoleTypeObject task : trustedRoles) {
                for (IRole role : author.getRolesForGuild(guild)) {
                    if (role.getID().equals(task.getRoleID())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void addTrusted(String roleID) {
        for (RoleTypeObject r : trustedRoles) {
            if (r.getRoleID().equals(roleID)) {
                return;
            }
        }
        trustedRoles.add(new RoleTypeObject(Globals.getClient().getRoleByID(roleID).getName(), roleID));
    }

    public void delTrusted(String roleID) {
        int i = 0;
        for (RoleTypeObject r : trustedRoles) {
            if (r.getRoleID().equals(roleID)) {
                trustedRoles.remove(i);
                return;
            }
            i++;
        }
    }

    public void setMaxMentionLimit(int maxMentionLimit) {
        this.maxMentionLimit = maxMentionLimit;
    }

    public void addOffender(OffenderObject offenderObject) {
        repeatOffenders.add(offenderObject);
    }

    public ArrayList<OffenderObject> getRepeatOffenders() {
        return repeatOffenders;
    }

    public void addOffence(String userID) {
        for (int i = 0; i < repeatOffenders.size(); i++) {
            if (repeatOffenders.get(i).getID().equals(userID)) {
                repeatOffenders.get(i).addOffence();
            }
        }
    }

    public void setMutedRole(RoleTypeObject mutedRole) {
        this.mutedRole = mutedRole;
    }

    public RoleTypeObject getMutedRole() {
        return mutedRole;
    }

    public ArrayList<RoleTypeObject> getTrustedRoles() {
        return trustedRoles;
    }

    public boolean isRoleCosmetic(String id) {
        for (RoleTypeObject r : cosmeticRoles) {
            if (r.getRoleID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRoleModifier(String id) {
        for (RoleTypeObject r : modifierRoles) {
            if (r.getRoleID().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
