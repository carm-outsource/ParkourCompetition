package cc.carm.outsource.plugin.parkourcompetition.listener;

import cc.carm.lib.easylistener.EasyListener;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginConfig;
import cc.carm.outsource.plugin.parkourcompetition.conf.PluginMessages;
import cc.carm.outsource.plugin.parkourcompetition.util.FireworkUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class FireworkListener extends EasyListener {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public FireworkListener(Plugin plugin) {
        super(plugin);
        
        handle(PlayerQuitEvent.class, (e) -> cooldown.remove(e.getPlayer().getUniqueId()));
        handleEvent(EntityDamageByEntityEvent.class)
                .filter(e -> e.getDamager() instanceof Firework)
                .filter(e -> PluginConfig.FIREWORK.AREA.getNotNull().isInArea(e.getEntity().getLocation()))
                .cancel();  // 取消烟花秀带来的爆炸伤害
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        if (!PluginConfig.FIREWORK.AREA.getNotNull().isInArea(player.getLocation())) return; // 判断玩家区域

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return; // 没有手持物品

        Material requiredType = PluginConfig.FIREWORK.ITEM_TYPE.get();
        if (requiredType == null || item.getType() != requiredType) return; //物品类型不对


        if (hasCooldown(player)) {
            PluginMessages.FIREWORK.IN_COOLDOWN.send(player, getCooldown(player));
            return;
        }

        // 发射烟花
        Location location = player.getLocation();
        FireworkUtils.spawnFirework(location.clone().add(0, 2, 0));

        updateCooldown(player); //更新冷却时间

    }

    public boolean hasCooldown(Player player) {
        return !player.isOp() && getCooldown(player) > 0;
    }

    public double getCooldown(Player player) {
        long lastTime = this.cooldown.getOrDefault(player.getUniqueId(), -1L);
        if (lastTime < 0) return 0D;

        int cooldown = PluginConfig.FIREWORK.COOLDOWN.getNotNull();
        return ((double) (lastTime - (System.currentTimeMillis() - cooldown))) / 1000;
    }

    public void updateCooldown(Player player) {
        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }

}
