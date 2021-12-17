package com.zpedroo.customenchants;

import com.zpedroo.customenchants.enchants.thunder.Thunder;
import com.zpedroo.customenchants.listeners.PlayerGeneralListeners;
import com.zpedroo.customenchants.managers.EnchantManager;
import com.zpedroo.customenchants.utils.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomEnchants extends JavaPlugin {

    private static CustomEnchants instance;
    public static CustomEnchants get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);
        new EnchantManager();

        registerListeners();
        loadCustomEnchants();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void loadCustomEnchants() {
        new Thunder(this);
    }
}