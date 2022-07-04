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


    public static final class GAME {

        public static final ConfiguredMessageList<BaseComponent[]> STARTED = list()
                .defaults("&c&l无法参赛。 &f比赛已经开始了哦，欢迎下次参赛~")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> JOIN = list()
                .defaults("&a&l参赛成功！&f您将在游戏开始时传送到对应比赛地点。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> QUIT = list()
                .defaults("&a&l退赛完成！&f请自行通过菜单 或 [&6点击这里](command=/c) &f离开准备区域。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> FINISH = list()
                .defaults("&e&l恭喜！&f您完成了本次跑酷比赛，总用时 &6%(time)秒 &f，排名为 &6#%(index) &f！")
                .params("time", "index").build();

        public static final ConfiguredMessageList<BaseComponent[]> FINISH_OTHER = list()
                .defaults("&f恭喜 &e&l%(player) &f作为 &6第%(index)名 &f完成了本次跑酷比赛，用时 &6%(time)秒 &f！")
                .params("player", "time", "index").build();


        public static final ConfiguredMessageList<BaseComponent[]> END = list()
                .defaults(
                        "&e&l比赛结束！&f比赛的前三名已经产生，分别是：",
                        "  &8#%(p1_index) &6&l%(p1_name) &f用时 &6%(p1_time)秒",
                        "  &8#%(p2_index) &6&l%(p2_name) &f用时 &6%(p2_time)秒",
                        "  &8#%(p3_index) &6&l%(p3_name) &f用时 &6%(p3_time)秒",
                        "&f未完成的也气馁，继续努力，我们仍有下次比赛哦~"
                ).params(
                        "p1_index", "p1_name", "p1_time",
                        "p2_index", "p2_name", "p2_time",
                        "p3_index", "p3_name", "p3_time"
                ).build();

    }

    public static final class FIREWORK {

        public static final ConfiguredMessageList<BaseComponent[]> IN_COOLDOWN = list()
                .defaults("&f您需要等待 &c%(cooldown) &f秒才能再次发射烟花哦~")
                .params("cooldown").build();
    }

}
