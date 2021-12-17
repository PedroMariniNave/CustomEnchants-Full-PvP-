package com.zpedroo.customenchants.objects;

import org.bukkit.plugin.Plugin;

public class Listener {

    private Plugin plugin;
    private org.bukkit.event.Listener listener;

    public Listener(Plugin plugin, org.bukkit.event.Listener listener) {
        this.plugin = plugin;
        this.listener = listener;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}