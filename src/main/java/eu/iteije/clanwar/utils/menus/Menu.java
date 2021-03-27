package eu.iteije.clanwar.utils.menus;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Menu implements InventoryHolder {

    private final String name;
    @Getter private final Inventory inventory;
    private final Map<Integer, Item> slots = new HashMap<>();

    public Menu(String title, int size) {
        this(title, size, "general");
    }

    public Menu(String title, int size, String permission) {
        this.name = permission;
        this.inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));
    }

    public void setItem(int slot, Item item) {
        this.slots.put(slot, item);
        this.inventory.setItem(slot, item.getItem());
    }

    public void onClick(int slot, Player player) {
        Item clicked = slots.get(slot);
        if (clicked == null) return;

        clicked.onClick(player);
    }

    public void open(Player player) {
        if (player.hasPermission("clanwar.menu." + name)) {
            player.openInventory(this.inventory);
        }
    }

    public void fill(Point start, Point end, boolean borderOnly, Material material) {
        int deltaX = end.x - start.x;
        ItemStack stack = new ItemStack(material, 1);

        if (borderOnly) {
            // fill top and bottom row
            for (int x = start.x; x <= end.x; x++) {
                int topRow = start.y * 9;
                inventory.setItem(topRow + x, stack);

                int bottomRow = end.y * 9;
                inventory.setItem(bottomRow + x, stack);
            }

            // fill right and left column
            for (int y = start.y + 1; y < end.y; y++) {
                int column = y * 9;

                inventory.setItem(column + start.x, stack);
                inventory.setItem(column + (start.x == 0 ? deltaX : deltaX + start.x), stack);
            }
        } else {
            // fill every row
            for (int y = start.y; y <= end.y; y++) {
                int column = y * 9;

                for (int x = start.x; x <= end.x; x++) {
                    inventory.setItem(column + x, stack);
                }
            }
        }
    }

}
