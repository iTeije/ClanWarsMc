package eu.iteije.clanwar.npcs.listeners;

import eu.iteije.clanwar.ClanWar;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    private final ClanWar instance;

    public NPCClickListener(ClanWar instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        this.onLeftClick(new NPCLeftClickEvent(event.getNPC(), event.getClicker()));
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {

    }

}
