package com.zpedroo.customenchants.managers;

import com.zpedroo.customenchants.cache.DataCache;
import com.zpedroo.customenchants.objects.EnchantProperties;
import com.zpedroo.customenchants.utils.FileUtils;
import org.bukkit.ChatColor;

public class EnchantManager {

    private static EnchantManager instance;
    public static EnchantManager getInstance() { return instance; }

    private DataCache dataCache;

    public EnchantManager() {
        instance = this;
        this.dataCache = new DataCache();
        this.loadConfigEnchants();
    }

    public EnchantProperties getEnchantmentProperties(String enchantName) {
        return dataCache.getEnchants().get(enchantName);
    }

    private void loadConfigEnchants() {
        FileUtils.Files file = FileUtils.Files.CONFIG;
        for (String enchantName : FileUtils.get().getSection(file, "Enchantments")) {
            String display = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Enchantments." + enchantName + ".display"));
            int maxLevel = FileUtils.get().getInt(file, "Enchantments." + enchantName + ".max-level");
            int expPerLevel = FileUtils.get().getInt(file, "Enchantments." + enchantName + ".exp-per-level");

            cache(new EnchantProperties(enchantName.toUpperCase(), display, maxLevel, expPerLevel));
        }
    }

    private void cache(EnchantProperties enchantProperties) {
        dataCache.getEnchants().put(enchantProperties.getName(), enchantProperties);
    }
}