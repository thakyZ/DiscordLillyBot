package GuildToggles;

import GuildToggles.Modules.*;
import GuildToggles.Toggles.*;

import java.util.ArrayList;

/**
 * Created by Vaerys on 20/02/2017.
 */
public class ToggleInit {
    public static ArrayList<GuildToggle> get(){
        ArrayList<GuildToggle> guildToggles = new ArrayList<>();

        //toggles
        guildToggles.add(new AdminLogging());
        guildToggles.add(new CompEntries());
        guildToggles.add(new DailyMessage());
        guildToggles.add(new DeleteLogging());
        guildToggles.add(new DenyInvites());
        guildToggles.add(new GeneralLogging());
        guildToggles.add(new LoginMessage());
        guildToggles.add(new MentionSpam());
        guildToggles.add(new MuteRepeatOffender());
        guildToggles.add(new ShitpostFiltering());
        guildToggles.add(new Voting());
        guildToggles.add(new RateLimiting());
        guildToggles.add(new JoinLeaveLgging());
        guildToggles.add(new EditLogging());
        guildToggles.add(new UserRoleLogging());

        //modules
        guildToggles.add(new ModuleChars());
        guildToggles.add(new ModuleComp());
        guildToggles.add(new ModuleServers());
        guildToggles.add(new ModuleRoles());
        guildToggles.add(new ModuleCC());
        guildToggles.add(new ModuleMe());

        return guildToggles;
    }
}
