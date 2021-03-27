package eu.iteije.clanwar.utils.serializers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class LocationSerializer {

    public static String serializeLocation(Location location) {
        return location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getPitch() + ";" + location.getYaw() + ";" + location.getWorld().getUID();
    }

    public static Location deserializeLocation(String serialized) {
        String[] data = serialized.split(";");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        float pitch = Float.parseFloat(data[3]);
        float yaw = Float.parseFloat(data[4]);
        World world = Bukkit.getWorld(UUID.fromString(data[5]));

        return new Location(world, x, y, z, yaw, pitch);
    }

}
