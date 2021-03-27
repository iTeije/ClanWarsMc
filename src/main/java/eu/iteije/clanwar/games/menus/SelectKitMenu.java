package eu.iteije.clanwar.games.menus;

import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.games.kits.GameKitModule;
import eu.iteije.clanwar.games.kits.objects.Kit;
import eu.iteije.clanwar.utils.menus.Item;
import eu.iteije.clanwar.utils.menus.Menu;
import org.bukkit.Material;

import java.awt.*;
import java.util.List;

public class SelectKitMenu extends Menu {

    public SelectKitMenu(GameKitModule kitModule, GameModule gameModule) {
        super("&8Select Kit", 54, "selectkit");

        // Fill border with black stained glass panes
        fill(new Point(0, 0), new Point(8, 5), true, Material.BLACK_STAINED_GLASS_PANE);

        List<Integer> slots = super.calculateSlots(new Point(1, 1), new Point(7, 4));
        int index = 0;
        for (Kit kit : kitModule.getKits()) {
            setItem(slots.get(index), new Item(kit.getIcon())
                    .setName("&d" + kit.getName())
                    .setOnClick((player, item) -> {
                        // TODO Join game
                        // TODO Apply kit
                    })
            );

            index++;
        }

        // Close inventory item
        setItem(49, new Item(Material.BARRIER).setOnClick((player, item) -> player.closeInventory()));

    }
}
