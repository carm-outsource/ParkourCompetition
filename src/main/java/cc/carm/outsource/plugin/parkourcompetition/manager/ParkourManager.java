package cc.carm.outsource.plugin.parkourcompetition.manager;

import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;
import cc.carm.outsource.plugin.parkourcompetition.util.CountdownRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ParkourManager {

    protected final Set<UUID> joinedPlayers = new HashSet<>();
    protected final Map<UUID, Long> finishTime = new LinkedHashMap<>();
    protected @Nullable Long startTime;
    protected boolean ended = false;


    public void prepareGame() {

        // 把空气替换为空气墙
        PluginConfig.GAME.WALL_BLOCKS.getNotNull().executeChange(Material::isAir, Material.BARRIER);

        // 吧所有玩家传送到起点
        Location startLoc = PluginConfig.LOCATIONS.GAME.getNotNull();
        getJoinedPlayers().forEach(p -> p.teleport(startLoc));

        // 启动计时器
        CountdownRunnable.start(5, t -> {
            for (Player player : getJoinedPlayers()) {
                PluginConfig.GAME.PREPARE.TITLE.send(player, t);
                PluginConfig.GAME.PREPARE.SOUND.playTo(player);
            }
        }, this::startGame);
    }


    public void startGame() {
        this.startTime = System.currentTimeMillis();

        // 把空气墙替换为空气
        PluginConfig.GAME.WALL_BLOCKS.getNotNull().executeChange(m -> m == Material.BARRIER, Material.AIR);

        // 发送通知
        for (Player player : getJoinedPlayers()) {
            PluginConfig.GAME.START.TITLE.send(player);
            PluginConfig.GAME.START.SOUND.playTo(player);
        }

    }

    public void endGame() {
        if (startTime == null) return;
        if (isEnded()) return;

        this.ended = true;

        List<Object> params = new ArrayList<>();

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        Iterator<Map.Entry<UUID, Long>> iterator = this.finishTime.entrySet().iterator();
        int i = 1;
        while (true) {
            if (!iterator.hasNext()) break;
            Map.Entry<UUID, Long> entry = iterator.next();
            params.add(i);
            params.add(Bukkit.getOfflinePlayer(entry.getKey()).getName());

            long cost = entry.getValue() - startTime;
            params.add(format.format((double) cost / 1000D));

            i++;
            if (i > 3) break;
        }

        // 发送通知
        Object[] finalParams = params.toArray();

        PluginMessages.GAME.END.broadcast(finalParams);
        PluginConfig.GAME.END.SOUND.playToAll();
    }

    public boolean isStarted() {
        return startTime != null;
    }

    public boolean isEnded() {
        return ended;
    }

    public @Nullable Long getStartTime() {
        return startTime;
    }

    public Set<UUID> getJoinedUUIDs() {
        return joinedPlayers;
    }

    public Set<Player> getJoinedPlayers() {
        return getJoinedUUIDs().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Map<UUID, Long> getFinishTime() {
        return finishTime;
    }


}
