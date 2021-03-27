package eu.iteije.clanwar.games.kits.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class KitArmor {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    public void apply(Player player) {
        player.getInventory().setHelmet(getArmorPiece(helmet));
        player.getInventory().setChestplate(getArmorPiece(chestplate));
        player.getInventory().setLeggings(getArmorPiece(leggings));
        player.getInventory().setBoots(getArmorPiece(boots));
    }

    private ItemStack getArmorPiece(ItemStack stack) {
        return stack == null ? new ItemStack(Material.AIR) : stack;
    }

}
