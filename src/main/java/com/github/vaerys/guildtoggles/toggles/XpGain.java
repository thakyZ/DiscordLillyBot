package com.github.vaerys.guildtoggles.toggles;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.pogos.GuildConfig;
import com.github.vaerys.templates.GuildSetting;

/**
 * Created by Vaerys on 04/07/2017.
 */
public class XpGain extends GuildSetting {
    @Override
    public String name() {
        return "XpGain";
    }

    @Override
    public boolean toggle(GuildConfig config) {
        return config.xpGain = !config.xpGain;
    }

    @Override
    public boolean get(GuildConfig config) {
        return config.xpGain;
    }

    @Override
    public boolean getDefault() {
        return new GuildConfig().xpGain;
    }

    @Override
    public String desc(CommandObject command) {
        return "Enables the ability to gain pixels.";
    }

    @Override
    public void setup() {

    }
}
