package cc.carm.outsource.plugin.parkourcompetition.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);


}
