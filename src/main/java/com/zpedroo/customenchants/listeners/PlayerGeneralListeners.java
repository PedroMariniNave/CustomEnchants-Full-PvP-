package com.zpedroo.customenchants.listeners;

import com.zpedroo.customenchants.managers.EnchantManager;
import com.zpedroo.customenchants.objects.EnchantProperties;
import com.zpedroo.customenchants.utils.config.Messages;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBookClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;

        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        NBTItem cursorNBT = new NBTItem(cursor);
        if (!cursorNBT.hasKey("CustomEnchant")) return;

        event.setCancelled(true);

        String customEnchant = cursorNBT.getString("CustomEnchant");

        ItemStack item = currentItem.clone();
        NBTItem currentNBT = new NBTItem(item);
        if (currentNBT.hasKey(customEnchant)) {
            player.sendMessage(Messages.MAX_LEVEL);
            return;
        }

        List<String> compatibleItems = cursorNBT.getObject("CompatibleItems", List.class);
        if (!compatibleItems.contains(item.getType().toString())) {
            player.sendMessage(Messages.INCOMPATIBLE_ITEM);
            return;
        }

        int playerLevel = player.getLevel();
        int expLevelCost = cursorNBT.getInteger("ExpCost");

        if (playerLevel < expLevelCost) {
            player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_EXP, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    String.valueOf(player.getLevel()),
                    String.valueOf(expLevelCost)
            }));
            return;
        }

        currentNBT.addCompound(customEnchant);

        String enchantDisplay = cursorNBT.getString("EnchantDisplay");
        ItemMeta meta = currentNBT.getItem().getItemMeta();

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>(1);
        lore.add(enchantDisplay);

        meta.setLore(lore);

        cursor.setAmount(cursor.getAmount() - 1);
        event.setCursor(cursor);

        currentNBT.getItem().setItemMeta(meta);

        player.getInventory().setItem(event.getSlot(), currentNBT.getItem());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVanillaBookClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;

        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        ItemStack cursor = event.getCursor();
        if (!cursor.getType().equals(Material.ENCHANTED_BOOK)) return;

        ItemStack currentItem = event.getCurrentItem();
        EnchantmentStorageMeta cursorEnchantStorage = (EnchantmentStorageMeta) cursor.getItemMeta();
        if (cursorEnchantStorage.getStoredEnchants().isEmpty()) return;

        Enchantment firstEnchant = cursorEnchantStorage.getStoredEnchants().keySet().stream().findFirst().get();
        EnchantProperties enchantProperties = EnchantManager.getInstance().getEnchantmentProperties(firstEnchant.getName());
        if (enchantProperties == null) return;

        event.setCancelled(true);

        if (currentItem.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta currentEnchantStorage = (EnchantmentStorageMeta) currentItem.getItemMeta();

            if (!cursorEnchantStorage.hasStoredEnchant(firstEnchant)) return; // different book
            if (cursorEnchantStorage.getStoredEnchantLevel(firstEnchant) != currentEnchantStorage.getStoredEnchantLevel(firstEnchant)) return; // different level
            if (cursorEnchantStorage.getStoredEnchantLevel(firstEnchant) >= enchantProperties.getMaxLevel()) {
                player.sendMessage(Messages.MAX_LEVEL);
                return; // different level
            }

            currentEnchantStorage.addStoredEnchant(firstEnchant, currentEnchantStorage.getStoredEnchantLevel(firstEnchant) + 1, true);
            currentItem.setItemMeta(currentEnchantStorage);

            cursor.setAmount(cursor.getAmount() - 1);
            event.setCursor(cursor);

            player.updateInventory();
            return;
        }

        if (!firstEnchant.canEnchantItem(currentItem)) {
            player.sendMessage(Messages.INCOMPATIBLE_ITEM);
            return;
        }

        int playerLevel = player.getLevel();
        int expLevelCost = enchantProperties.getExpPerLevel() * cursorEnchantStorage.getStoredEnchantLevel(firstEnchant);

        if (playerLevel < expLevelCost) {
            player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_EXP, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    String.valueOf(player.getLevel()),
                    String.valueOf(expLevelCost)
            }));
            return;
        }

        int enchantmentLevel = currentItem.getEnchantmentLevel(firstEnchant);
        int levelToAdd = cursorEnchantStorage.getStoredEnchantLevel(firstEnchant);

        if (currentItem.getItemMeta().hasEnchant(firstEnchant)) {
            if (cursorEnchantStorage.getStoredEnchantLevel(firstEnchant) < enchantmentLevel) return;
            if (cursorEnchantStorage.getStoredEnchantLevel(firstEnchant) == enchantmentLevel) ++levelToAdd;
        }

        if (enchantmentLevel + levelToAdd > enchantProperties.getMaxLevel()) {
            player.sendMessage(Messages.MAX_LEVEL);
            return;
        }

        ItemMeta meta = currentItem.getItemMeta();
        if (meta == null) return;

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>(1);
        String loreLine = StringUtils.replace(enchantProperties.getDisplay(), "{level}", String.valueOf(levelToAdd));
        int line = lore.indexOf(StringUtils.replace(enchantProperties.getDisplay(), "{level}", String.valueOf(enchantmentLevel)));
        if (line == -1) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add(loreLine);
        } else {
            lore.set(line, loreLine);
        }

        meta.setLore(lore);
        meta.addEnchant(firstEnchant, levelToAdd, true);
        currentItem.setItemMeta(meta);

        player.setLevel(playerLevel - expLevelCost);

        cursor.setAmount(cursor.getAmount() - 1);
        event.setCursor(cursor);

        player.getInventory().setItem(event.getSlot(), currentItem);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnchant(EnchantItemEvent event) {
        if (event.getEnchantsToAdd().size() <= 1) return;
        if (event.getItem().getType() != Material.BOOK) return;

        Map<Enchantment, Integer> enchants = event.getEnchantsToAdd();
        Enchantment firstEnchant = enchants.keySet().stream().findFirst().get();

        for (Enchantment enchant : enchants.keySet()) {
            if (!firstEnchant.equals(enchant)) enchants.remove(enchant);
        }
    }
}