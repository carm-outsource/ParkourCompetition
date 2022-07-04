package cc.carm.outsource.plugin.parkourcompetition.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSerializable;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
import cc.carm.outsource.plugin.parkourcompetition.data.DataBlockLocation;
import cc.carm.outsource.plugin.parkourcompetition.data.DataCubeArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Objects;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({"在 HubParkour 插件中配置的跑酷ID"})
    public static final ConfigValue<Integer> PARKOUR_ID = ConfiguredValue.of(Integer.class, 1);


    @HeaderComment("游戏相关配置")
    public static final class GAME {

        @HeaderComment({"空气墙范围"})
        public static final ConfigValue<DataCubeArea> WALL_BLOCKS = ConfiguredSerializable.of(
                DataCubeArea.class, new DataCubeArea(
                        Objects.requireNonNull(Bukkit.getWorld("pmpc-1")),
                        new DataBlockLocation(-89, 78, 78),
                        new DataBlockLocation(-85, 83, 78)
                )
        );

        public static final class PREPARE {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.BLOCK_NOTE_BLOCK_PLING);

            public static final ConfiguredTitle TITLE = ConfiguredTitle.create()
                    .defaults("&f比赛将在 &e%(time) &f秒后开始", "&7&o请做好准备")
                    .params("time").fadeIn(0).stay(25).fadeOut(0)
                    .whenSend((player, fadeIn, stay, fadeOut, line1, line2) -> player.sendTitle(line1, line2, fadeIn, stay, fadeOut))
                    .build();

        }

        public static final class START {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5F);

            public static final ConfiguredTitle TITLE = ConfiguredTitle.create()
                    .defaults("&e&l比赛开始", "&f迅速到达终点取得前三名！")
                    .fadeIn(0).stay(25).fadeOut(10)
                    .whenSend((player, fadeIn, stay, fadeOut, line1, line2) -> player.sendTitle(line1, line2, fadeIn, stay, fadeOut))
                    .build();

        }


        public static final class FINISH {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_PLAYER_LEVELUP);

            public static final ConfiguredTitle TITLE = ConfiguredTitle.create()
                    .defaults("&e&l恭喜您完成了比赛！", "&f共耗时 %(time) &f秒")
                    .params("time").fadeIn(0).stay(100).fadeOut(40)
                    .whenSend((player, fadeIn, stay, fadeOut, line1, line2) -> player.sendTitle(line1, line2, fadeIn, stay, fadeOut))
                    .build();

        }

        public static final class END {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.UI_TOAST_CHALLENGE_COMPLETE);

        }


    }

    @HeaderComment({"玩家可以用来放烟花的工具"})
    public static final class FIREWORK {

        @HeaderComment({"烟花区域配置，在此区域内可以放烟花"})
        public static final ConfigValue<DataCubeArea> AREA = ConfiguredSerializable.of(
                DataCubeArea.class, new DataCubeArea(
                        Objects.requireNonNull(Bukkit.getWorld("pmpc")),
                        new DataBlockLocation(158, 120, -62),
                        new DataBlockLocation(265, 68, 6)
                )
        );

        @HeaderComment({"用来放烟花的物品类型,默认是火把，即玩家手持火把右键即可随机放烟花。"})
        public static final ConfigValue<Material> ITEM_TYPE = ConfigValue.builder()
                .asValue(Material.class).fromString()
                .parseValue((s, d) -> Material.matchMaterial(s))
                .serializeValue(Material::name)
                .defaults(Material.TORCH)
                .build();

        @HeaderComment({"玩家放烟花的冷却时间，单位为毫秒。默认为 1000 毫秒(1秒)。"})
        public static final ConfigValue<Integer> COOLDOWN = ConfiguredValue.of(Integer.class, 1000);

    }

    @HeaderComment({"传送点配置"})
    public static final class LOCATIONS {

        @HeaderComment({"等待区域的传送点"})
        public static final ConfiguredSerializable<Location> WAIT = ConfiguredSerializable.of(
                Location.class, new Location(Bukkit.getWorld("pmpc"), 207, 70, -113)
        );

        @HeaderComment({"游戏区域的传送点"})
        public static final ConfiguredSerializable<Location> GAME = ConfiguredSerializable.of(
                Location.class, new Location(Bukkit.getWorld("pmpc-1"), -87, 78, 82)
        );

        @HeaderComment("结束区域的传送点")
        public static final ConfiguredSerializable<Location> FINISH = ConfiguredSerializable.of(
                Location.class, new Location(Bukkit.getWorld("pmpc"), 207, 93, -32)
        );

        @HeaderComment({"颁奖台的传送点位置"})
        public static final ConfiguredSerializable<Location> PODIUM = ConfiguredSerializable.of(
                Location.class, new Location(Bukkit.getWorld("pmpc"), 207, 95, -54)
        );

    }


}
