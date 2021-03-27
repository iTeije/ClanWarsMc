package eu.iteije.clanwar.npcs;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.npcs.listeners.NPCClickListener;
import eu.iteije.clanwar.resources.PluginFile;
import eu.iteije.clanwar.utils.LocationSerializer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;

public class NpcModule {

    private NPC npc;

    private final PluginFile configFile;

    public NpcModule(ClanWar instance, PluginFile configFile) {
        this.configFile = configFile;
        this.npc = getJoinNpc(configFile);

        PluginManager manager = instance.getServer().getPluginManager();
        manager.registerEvents(new NPCClickListener(instance), instance);
    }

    private NPC getJoinNpc(PluginFile configFile) {
        String serializedLocation = configFile.getConfiguration().getString("npc");
        if (serializedLocation == null) {
            System.out.println("[ClanWar] Unable to place join NPC, no location was found.");
            return null;
        }

        Location location = LocationSerializer.deserializeLocation(serializedLocation);
        if (location != null) {
            return placeNpc(location, false);
        } else {
            System.out.println("[ClanWar] Unable to place join NPC, location is invalid.");
        }
        return null;
    }

    public NPC placeNpc(Location location, boolean save) {
        if (this.npc != null) delete();

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "&dClick to join");
        this.setSkin(npc);

        npc.spawn(location, SpawnReason.PLUGIN);

        if (save) {
            configFile.getConfiguration().set("npc", LocationSerializer.serializeLocation(location));
            configFile.save();

            this.npc = npc;
        }

        return npc;
    }

    public void delete() {
        if (this.npc != null) {
            this.npc.destroy();
            CitizensAPI.getNPCRegistry().deregister(this.npc);
            this.npc = null;
        }
    }

    private NPC setSkin(NPC npc) {
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(
                "639601793",
                "x7fGrW9QImG5fIVOpPiMqXCAEJKqUFck/yAqkY+xCyaFIVHJz9K/q+3vf/ESVfTbGoVU9yt2d1FKAb3OrV0G/tfmz55TFa3TDQdBGweGiViaexPNwPHKHxlychvSqD6DKWofuUOdhJIwzx+uraGU4310oZ6JeKxWuAuBJZtSDBBn+Vlfriqsh7CrZ43bupDENu67iMTuuJI/e2NoKYUumG16TYJI4CdtiRPQHM4kQhDQWma/CjCxzdaLTeZg14uJoR3TUEmsD35yzyhm9iU4eHia3Oo7YTYVnGZTlkJqYODfhUZB6D+FspV9RuLEAawQB6hRKcKoQvTdEQF05FzyZwY72lhofeAKNnhmQQXgxQWWZpJqvSgAP4webO6btCaTdRGmkuuteIP85abUjHUMMxz6uYiEWoeh3FvQ9IChz9UZPRf4i6wiPMOuedJAbnL+n3wXYr+7+6lbfqhK6Q6YRe4MC8ouwb4SSUJ8KNuBhkCK7438up2fA+yMVwABcckJ6yVHoyAneAmPJpUvT76qzZsPDYv9QVtrUOpquS26eDlF1pVI4S5LbdsakYRJFr1CwMduGdGeY1xYnAdM+MBwBzHjQdYfsNJHUWt1ze28UUTOZrZVAcpXoVFD4bfL5gi35Ob/sBUECgzfM/p2ZH5h2ulby7dX3c9F3vNDVkGyDP8=",
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMTI4NjE1MTk1NywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NjRlMWEzODg5YzFiNDI5NzgwNDE2MDY0NTY4NGE1NGE2ZGNlY2VmMzc1Nzc5OWRlYTVmMzc3NDNhMWI1MjY0IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
        );
        return npc;
    }





}
