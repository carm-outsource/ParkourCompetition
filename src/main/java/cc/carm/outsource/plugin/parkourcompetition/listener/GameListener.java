package cc.carm.outsource.plugin.parkourcompetition.listener;

import cc.carm.lib.easylistener.EasyListener;
import cc.carm.outsource.plugin.parkourcompetition.Main;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;
import cc.carm.outsource.plugin.parkourcompetition.manager.ParkourManager;
import me.block2block.hubparkour.api.events.player.ParkourPlayerFinishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import java.text.NumberFormat;
import java.util.UUID;

public class GameListener extends EasyListener {

    public GameListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFinish(ParkourPlayerFinishEvent event) {
        if (event.getParkour().getId() != PluginConfig.PARKOUR_ID.getNotNull()) return;
        ParkourManager parkour = Main.getParkourManager();
        Long startTime = parkour.getStartTime();
        if (startTime == null) return;

        Player player = event.getPlayer().getPlayer();
        UUID uuid = player.getUniqueId();

        if (!parkour.getJoinedUUIDs().contains(uuid)) return;

        long cost = System.currentTimeMillis() - startTime;
        parkour.getFinishTime().put(uuid, cost);

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        String seconds = format.format((double) cost / 1000D);

        PluginConfig.GAME.FINISH.SOUND.playTo(player);
        PluginConfig.GAME.FINISH.TITLE.send(player, seconds);
        PluginMessages.GAME.FINISH.send(player, seconds, parkour.getFinishTime().size());

        if (parkour.getFinishTime().size() <= 3) {
            player.teleport(PluginConfig.LOCATIONS.PODIUM.getNotNull());
            if (parkour.getFinishTime().size() == 3) parkour.endGame();
        } else if (parkour.getFinishTime().size() > 3) {
            player.teleport(PluginConfig.LOCATIONS.FINISH.getNotNull());
        }

    }

}
