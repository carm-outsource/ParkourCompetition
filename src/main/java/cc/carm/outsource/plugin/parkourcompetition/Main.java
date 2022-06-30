package cc.carm.outsource.plugin.parkourcompetition;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;

public class Main extends EasyPlugin {

    private static Main instance;

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;


    @Override
    public boolean initialize() {
        instance = this;

        log("加载插件配置文件...");
        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);

        return true;
    }

    @Override
    protected void shutdown() {

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

}