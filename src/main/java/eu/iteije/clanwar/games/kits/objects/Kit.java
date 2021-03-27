package eu.iteije.clanwar.games.kits.objects;

import eu.iteije.clanwar.games.kits.enums.PotionTypes;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final String name;
    private final List<ItemStack> items;
    private final List<PotionEffect> effects;
    private final KitArmor armor;

    public Kit(String name, PluginFile kitFile) {
        this.name = name;

        FileConfiguration config = kitFile.getConfiguration();
        this.items = this.getItems(config.getConfigurationSection("kits." + name + ".items"));
        this.armor = this.getArmor(config.getConfigurationSection("kits." + name + ".gear"));
        this.effects = this.getEffects(config.getStringList("kits." + name + ".effects"));
    }

    public void apply(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(items.toArray(new ItemStack[0]));

        this.armor.apply(player);

        for (PotionEffect effect : this.effects) {
            effect.apply(player);
        }
    }

    private List<PotionEffect> getEffects(List<String> serializedEffects) {
        if (serializedEffects.size() == 0) return new ArrayList<>();
        List<PotionEffect> effects = new ArrayList<>();
        for (String effect : serializedEffects) {
            String[] data = effect.split(":");
            PotionEffectType effectType = PotionEffectType.getByName(data[0]);
            if (effectType == null) continue;

            int amplifier = data[1] != null ? Integer.parseInt(data[1]) - 1 : 1;

            this.effects.add(effectType.createEffect(3600 * 20, amplifier));
        }

        return effects;
    }

    private KitArmor getArmor(ConfigurationSection data) {
        return new KitArmor(
                getArmor(data, "HELMET"),
                getArmor(data, "CHESTPLATE"),
                getArmor(data, "LEGGINGS"),
                getArmor(data, "BOOTS")
        );
    }

    private ItemStack getArmor(ConfigurationSection data, String armorPiece) {
        if (data == null) return null;
        Material material = Material.getMaterial(data.getString(armorPiece + ".item"));
        ItemStack item = new ItemStack(material);

        List<String> enchantments = data.getStringList(armorPiece + ".enchantments");
        if (enchantments.size() != 0) item.setItemMeta(getEnchantments(item, enchantments));

        return item;
    }

    private List<ItemStack> getItems(ConfigurationSection data) {
        if (data == null) {
            System.out.println("[ClanWar] No items have been found for kit " + this.name);
            return new ArrayList<>();
        }

        List<ItemStack> itemList = new ArrayList<>();

        for (String itemKey : data.getKeys(false)) {
            ConfigurationSection itemData = data.getConfigurationSection(itemKey);
            if (itemData == null) continue;

            try {
                Material material = Material.getMaterial(itemData.getString("item"));
                int amount = itemData.getInt("amount");
                ItemStack item = new ItemStack(material, amount);

                boolean hasPotionMeta = itemData.getString("potionmeta") != null;
                if (hasPotionMeta) item.setItemMeta(getPotionMeta(item, itemData.getString("potionmeta")));

                List<String> enchantments = itemData.getStringList("enchantments");
                if (enchantments.size() != 0) item.setItemMeta(getEnchantments(item, enchantments));

                itemList.add(item);
            } catch (NullPointerException | IllegalArgumentException exception) {
                System.out.println("[ClanWar] Kit " + this.name + " has an invalid item '" + itemKey + "'");
            }
        }

        return itemList;
    }

    private PotionMeta getPotionMeta(ItemStack item, String serialized) throws NumberFormatException, NullPointerException {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        String[] data = serialized.split(":");

        PotionEffectType effectType = PotionEffectType.getByName(data[0]);
        meta.addCustomEffect(new PotionEffect(
                effectType, Integer.parseInt(data[1]) * 20, Integer.parseInt(data[2])
        ), true);

        meta.setColor(PotionTypes.getByEffect(effectType).getColor());

        return meta;
    }

    private ItemMeta getEnchantments(ItemStack item, List<String> enchantments) {
        ItemMeta meta = item.getItemMeta();
        for (String serialized : enchantments) {
            String[] data = serialized.split(":");
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(data[0].toLowerCase()));
            meta.addEnchant(enchantment, Integer.parseInt(data[1]), true);
        }
        return meta;
    }

}
