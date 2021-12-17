package com.zpedroo.customenchants.enchants.thunder;

import com.zpedroo.customenchants.objects.Command;
import com.zpedroo.customenchants.objects.Listener;
import com.zpedroo.customenchants.utils.FileUtils;
import com.zpedroo.customenchants.utils.builder.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static com.zpedroo.customenchants.enchants.thunder.Thunder.Settings.*;

public class Thunder {

    private static Thunder instance;
    public static Thunder getInstance() { return instance; }

    private ItemStack item;

    public Thunder(Plugin plugin) {
        instance = this;
        this.item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.THUNDER).get(), "Enchant-Item").build();
        new Command(COMMAND, ALIASES, PERMISSION, PERMISSION_MESSAGE, new ThunderCmd()).register();
        new Listener(plugin, new ThunderListeners()).register();
    }

    public ItemStack getItem() {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setString("CustomEnchant", "Thunder");
        nbt.setObject("CompatibleItems", COMPATIBLE_ITEMS);
        nbt.setString("EnchantDisplay", ENCHANT_DISPLAY);
        nbt.setInteger("ExpCost", EXP_COST);

        return nbt.getItem();
    }

    static class Settings {

        public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.THUNDER, "Settings.command");

        public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.THUNDER, "Settings.aliases");

        public static final String PERMISSION = FileUtils.get().getString(FileUtils.Files.THUNDER, "Settings.permission");

        public static final String PERMISSION_MESSAGE = FileUtils.get().getColoredString(FileUtils.Files.THUNDER, "Settings.permission-message");

        public static final List<String> COMPATIBLE_ITEMS = FileUtils.get().getStringList(FileUtils.Files.THUNDER, "Settings.compatible-items");

        public static final double CHANCE_PER_HIT = FileUtils.get().getDouble(FileUtils.Files.THUNDER, "Settings.chance-per-hit");

        public static final double THUNDER_DAMAGE = FileUtils.get().getInt(FileUtils.Files.THUNDER, "Settings.thunder-damage");

        public static final int EXP_COST = FileUtils.get().getInt(FileUtils.Files.THUNDER, "Settings.exp-cost");

        public static final String ENCHANT_DISPLAY = FileUtils.get().getColoredString(FileUtils.Files.THUNDER, "Settings.enchant-display");
    }

    static class Messages {

        public static final String COMMAND_USAGE = FileUtils.get().getColoredString(FileUtils.Files.THUNDER, "Messages.command-usage");
    }
}