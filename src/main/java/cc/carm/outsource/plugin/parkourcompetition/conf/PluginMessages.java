package cc.carm.outsource.plugin.parkourcompetition.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class PluginMessages extends ConfigurationRoot {

    protected static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser())
                .whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    protected static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser())
                .whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    private static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender == null) return MineDown.parse(message);
            if (sender instanceof Player player) {
                return MineDown.parse(PlaceholderAPI.setPlaceholders(player, message));
            } else {
                return MineDown.parse(message);
            }
        };
    }

    public static final ConfiguredMessage<String> LOAD_FAILED = ConfiguredMessage.asString()
            .defaults("&c您的用户数据未被正确加载，请重新进入！")
            .build();

}
