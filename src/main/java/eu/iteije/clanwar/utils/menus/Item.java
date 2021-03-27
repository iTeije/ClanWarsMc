package eu.iteije.clanwar.utils.menus;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiConsumer;

public class Item {

    @Getter private final ItemStack item;
    private BiConsumer<Player, Item> consumer;

    public Item(Material material) {
        this.item = new ItemStack(material, 1);
    }

    public Item(ItemStack stack) {
        this.item = stack;
    }

    public Item setName(String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public Item setOnClick(BiConsumer<Player, Item> onClick) {
        this.consumer = onClick;
        return this;
    }

    public void onClick(Player player) {
        this.consumer.accept(player, this);
    }

}
