package com.zpedroo.customenchants.objects;

import com.zpedroo.customenchants.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class Command {

    private String command;
    private List<String> aliases;
    private String permission;
    private String permissionMessage;
    private CommandExecutor executor;

    public Command(String command, List<String> aliases, String permission, String permissionMessage, CommandExecutor executor) {
        this.command = command;
        this.aliases = aliases;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.executor = executor;
    }

    public void register() {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, CustomEnchants.get());
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);
            pluginCmd.setPermission(permission);
            pluginCmd.setPermissionMessage(permissionMessage);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(CustomEnchants.get().getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}