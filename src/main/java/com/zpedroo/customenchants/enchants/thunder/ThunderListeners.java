package com.zpedroo.customenchants.enchants.thunder;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ThunderListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        ItemStack item = damager.getItemInHand();
        if (item == null || item.getType().equals(Material.AIR)) return;

        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("Thunder")) return;

        Random random = new Random();
        if (random.nextDouble() * 100D > Thunder.Settings.CHANCE_PER_HIT) return;

        Player player = (Player) event.getEntity();
        player.getWorld().spigot().strikeLightning(player.getLocation(), false);
        player.damage(event.getDamage() * Thunder.Settings.THUNDER_DAMAGE);
    }
}