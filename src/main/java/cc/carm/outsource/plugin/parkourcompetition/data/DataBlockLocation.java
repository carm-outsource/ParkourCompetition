package cc.carm.outsource.plugin.parkourcompetition.data;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("DataBlockLocation")
public class DataBlockLocation implements ConfigurationSerializable {

    private int x;
    private int y;
    private int z;

    public DataBlockLocation(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public DataBlockLocation(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DataBlockLocation(Map<String, Object> args) {
        this(NumberConversions.toInt(args.get("x")), NumberConversions.toInt(args.get("y")), NumberConversions.toInt(args.get("z")));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Contract("!null->!null")
    public @Nullable Location getBukkitLocation(@Nullable World world) {
        return new Location(world, getX(), getY(), getZ());
    }

    public @NotNull Block getBlock(@NotNull World world) {
        return world.getBlockAt(getX(), getY(), getZ());
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);

        return data;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

    public String serializeToText() {
        return getX() + ";" + getY() + ";" + getZ();
    }

    public static DataBlockLocation deserializeText(String s) {
        if (s == null || !s.contains(";")) return null;
        String[] args = StringUtils.split(s, ";");
        if (args.length != 3) return null;
        try {
            int x = NumberConversions.toInt(args[0]);
            int y = NumberConversions.toInt(args[1]);
            int z = NumberConversions.toInt(args[2]);
            return new DataBlockLocation(x, y, z);
        } catch (Exception ex) {
            return null;
        }
    }
}