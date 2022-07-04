package cc.carm.outsource.plugin.parkourcompetition;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.outsource.plugin.parkourcompetition.command.PluginCommand;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;
import cc.carm.outsource.plugin.parkourcompetition.data.DataBlockLocation;
import cc.carm.outsource.plugin.parkourcompetition.data.DataCubeArea;
import cc.carm.outsource.plugin.parkourcompetition.data.DataLocation;
import cc.carm.outsource.plugin.parkourcompetition.listener.FireworkListener;
import cc.carm.outsource.plugin.parkourcompetition.listener.GameListener;
import cc.carm.outsource.plugin.parkourcompetition.manager.ParkourManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Main extends EasyPlugin {

    private static Main instance;

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;

    protected ParkourManager parkourManager;

    @Override
    public boolean initialize() {
        instance = this;

        log("加载插件配置文件...");
        ConfigurationSerialization.registerClass(DataLocation.class);
        ConfigurationSerialization.registerClass(DataBlockLocation.class);
        ConfigurationSerialization.registerClass(DataCubeArea.class);

        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);

        log("初始化跑酷管理器...");
        this.parkourManager = new ParkourManager();

        log("加载监听器...");
        registerListener(new GameListener(this));
        registerListener(new FireworkListener(this));

        log("加载指令...");
        registerCommand("ParkourCompetition", new PluginCommand());

        return true;
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

    public static ParkourManager getParkourManager() {
        return getInstance().parkourManager;
    }

}