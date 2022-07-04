package cc.carm.outsource.plugin.parkourcompetition.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@SerializableAs("DataCubeArea")
public class DataCubeArea implements ConfigurationSerializable {

    protected final @NotNull World world;
    protected final @NotNull DataBlockLocation pos1;
    protected final @NotNull DataBlockLocation pos2;

    public DataCubeArea(Map<String, Object> args) {
        this.world = Objects.requireNonNull(Bukkit.getWorld((String) args.get("world")));
        this.pos1 = (DataBlockLocation) args.get("pos1");
        this.pos2 = (DataBlockLocation) args.get("pos2");
    }

    public DataCubeArea(@NotNull World world,
                        @NotNull DataBlockLocation pos1, @NotNull DataBlockLocation pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public @NotNull DataBlockLocation getPos1() {
        return pos1;
    }

    public @NotNull DataBlockLocation getPos2() {
        return pos2;
    }

    public boolean isInArea(Location location) {
        if (location.getWorld() == null) return false;
        if (!location.getWorld().equals(world)) return false;
        return isNumberInRange(location.getX(), getPos1().getX(), getPos2().getX())
                && isNumberInRange(location.getY(), getPos1().getY(), getPos2().getY())
                && isNumberInRange(location.getZ(), getPos1().getZ(), getPos2().getZ());
    }

    public int executeChange(Predicate<Material> predicate, Material finalType) {
        int i = 0;

        int maxX = Math.max(pos1.getX(), pos2.getX());
        int minX = Math.min(pos1.getX(), pos2.getX());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int minY = Math.min(pos1.getY(), pos2.getY());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (y < 1) continue; //不执行最后一层
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (predicate.test(block.getType())) {
                        block.setType(finalType);
                        i++;
                    }
                }
            }
        }
        return i;
    }

    public int countPredicate(Predicate<Material> predicate) {
        int i = 0;

        int maxX = Math.max(pos1.getX(), pos2.getX());
        int minX = Math.min(pos1.getX(), pos2.getX());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int minY = Math.min(pos1.getY(), pos2.getY());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (y < 1) continue; //不执行最后一层
                for (int z = minZ; z <= maxZ; z++) {
                    if (predicate.test(world.getBlockAt(x, y, z).getType())) {
                        i++;
                    }
                }
            }
        }
        return i;
    }

    public int countBlocks() {
        return Math.abs(getPos1().getX() - getPos2().getX()) *
                Math.abs(Math.max(1, getPos1().getY()) - Math.max(1, getPos2().getY())) *
                Math.abs(getPos1().getZ() - getPos2().getZ());
    }

    @Override
    public String toString() {
        return pos1 + " <-> " + pos2;
    }

    public String getRangeString() {
        return Math.abs(getPos1().getX() - getPos2().getX()) + " / " +
                Math.abs(Math.max(1, getPos1().getY()) - Math.max(1, getPos2().getY())) + " / " +
                Math.abs(getPos1().getZ() - getPos2().getZ());
    }

    public static @Nullable DataCubeArea create(@NotNull Location pos1, @NotNull Location pos2) {
        if (pos1.getWorld() == null || pos2.getWorld() == null) return null;
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())) return null;
        return new DataCubeArea(pos1.getWorld(), new DataBlockLocation(pos1), new DataBlockLocation(pos2));
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("world", world.getName());
        map.put("pos1", pos1);
        map.put("pos2", pos2);
        return map;
    }

    protected static boolean isNumberInRange(double source, double numberA, double numberB) {
        return Math.min(numberA, numberB) <= source && Math.max(numberA, numberB) >= source;
    }
}
