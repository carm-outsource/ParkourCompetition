package cc.carm.outsource.plugin.parkourcompetition.command;

import cc.carm.outsource.plugin.parkourcompetition.Main;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;
import cc.carm.outsource.plugin.parkourcompetition.manager.ParkourManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ParkourManager parkour = Main.getParkourManager();

        if (!(sender instanceof Player player)) {
            if (!parkour.isStarted()) {
                parkour.prepareGame();
                sender.sendMessage("开始比赛...");
            } else {
                sender.sendMessage("比赛已经开始了");
            }
            return true;
        }

        if (parkour.isStarted()) {
            PluginMessages.GAME.STARTED.send(player);
            return true;
        }

        if (parkour.getJoinedUUIDs().contains(player.getUniqueId())) {
            PluginMessages.GAME.QUIT.send(player);
            parkour.getJoinedUUIDs().remove(player.getUniqueId());
        } else {
            PluginMessages.GAME.JOIN.send(player);
            parkour.getJoinedUUIDs().add(player.getUniqueId());
            player.teleport(PluginConfig.LOCATIONS.WAIT.getNotNull());
        }

        return false;
    }


}
