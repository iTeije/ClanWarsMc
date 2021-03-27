package eu.iteije.clanwar.npcs.listeners;

import eu.iteije.clanwar.games.GameModule;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    private final GameModule gameModule;

    public NPCClickListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        this.onLeftClick(new NPCLeftClickEvent(event.getNPC(), event.getClicker()));
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        gameModule.openKitMenu(event.getClicker());
    }

}
