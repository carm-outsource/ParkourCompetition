package cc.carm.outsource.plugin.parkourcompetition.data;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SerializableAs("DataLocation")
public class DataLocation implements ConfigurationSerializable, Cloneable {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public DataLocation(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public DataLocation(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public DataLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public DataLocation(Map<String, Object> args) {
        this(
                NumberConversions.toDouble(args.get("x")),
                NumberConversions.toDouble(args.get("y")),
                NumberConversions.toDouble(args.get("z")),
                NumberConversions.toFloat(args.get("yaw")),
                NumberConversions.toFloat(args.get("pitch"))
        );
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Contract("!null->!null")
    public @Nullable Location getBukkitLocation(@Nullable World world) {
        return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
    }

    public void teleport(@NotNull Player player, @NotNull World world) {
        player.teleport(getBukkitLocation(world));
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataLocation that)) return false;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Float.compare(that.pitch, pitch) == 0 && Float.compare(that.yaw, yaw) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, pitch, yaw);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);

        data.put("yaw", this.yaw);
        data.put("pitch", this.pitch);

        return data;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z + " " + yaw + " " + pitch;
    }

    @Deprecated
    public String toSerializedString() {
        return serializeToText();
    }

    public String serializeToText() {
        if (getYaw() != 0 || getPitch() != 0) {
            return x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
        } else {
            return x + ";" + y + ";" + z;
        }
    }


    public String getPrettyString() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(x) + "&8/&f" + nf.format(y) + "&8/&f" + nf.format(z) + "&8/&f" + nf.format(yaw) + "&8/&f" + nf.format(pitch);
    }

    public static DataLocation deserializeText(String s) {
        if (s == null || !s.contains(";")) return null;
        String[] args = StringUtils.split(s, ";");
        if (args.length < 3) return null;
        try {
            double x = NumberConversions.toDouble(args[0]);
            double y = NumberConversions.toDouble(args[1]);
            double z = NumberConversions.toDouble(args[2]);
            float yaw = 0;
            float pitch = 0;
            if (args.length == 5) {
                yaw = NumberConversions.toFloat(args[3]);
                pitch = NumberConversions.toFloat(args[4]);
            }
            return new DataLocation(x, y, z, yaw, pitch);
        } catch (Exception ex) {
            return null;
        }
    }

    @Deprecated
    public static DataLocation parseString(String s) {
        return deserializeText(s);
    }

}