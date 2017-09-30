package com.github.vaerys.guildtoggles.toggles;

import com.github.vaerys.interfaces.GuildSetting;
import com.github.vaerys.masterobjects.GuildObject;
import com.github.vaerys.pogos.GuildConfig;

/**
 * Created by Vaerys on 20/02/2017.
 */
public class MentionSpam implements GuildSetting {

    @Override
    public String name() {
        return "MentionSpam";
    }

    @Override
    public boolean toggle(GuildConfig config) {
        return config.maxMentions = !config.maxMentions;
    }

    @Override
    public boolean get(GuildConfig config) {
        return config.maxMentions;
    }

    @Override
    public boolean getDefault() {
        return new GuildConfig().maxMentions;
    }

    @Override
    public void execute(GuildObject guild) {

    }
}
