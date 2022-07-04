package cc.carm.outsource.plugin.parkourcompetition.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public class FireworkUtils {


    public static void spawnFirework(@NotNull Location location) {
        spawnFirework(location, null);
    }

    public static void spawnFirework(@NotNull Location location, @Nullable Vector velocity) {

        Location target = location.clone().add(0, 2, 0);
        World world = Objects.requireNonNull(target.getWorld());

        Firework firework = (Firework) world.spawnEntity(target, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = buildFirework().build();

        meta.clearEffects();
        meta.addEffect(effect);
        meta.setPower(new Random().nextInt(3));

        firework.setFireworkMeta(meta);
        if (velocity != null) firework.setVelocity(velocity);

    }

    public static FireworkEffect.Builder buildFirework() {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        FireworkEffect.Type[] type = FireworkEffect.Type.values();
        Random random = new Random();

        builder.withColor(Color.fromRGB(
                random.nextInt(255), random.nextInt(255), random.nextInt(255)
        ));

        builder.with(type[random.nextInt(type.length)]);

        if (random.nextInt(3) == 0) builder.withTrail();
        if (random.nextInt(3) == 0) builder.withFlicker();

        if (random.nextInt(2) == 0) {
            builder.withFade(Color.fromRGB(
                    random.nextInt(255), random.nextInt(255), random.nextInt(255)
            ));
        }

        return builder;
    }

}
