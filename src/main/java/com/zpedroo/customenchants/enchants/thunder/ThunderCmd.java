package com.zpedroo.customenchants.enchants.thunder;

import com.zpedroo.customenchants.utils.config.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ThunderCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(StringUtils.replaceEach(Thunder.Messages.COMMAND_USAGE, new String[]{
                    "{cmd}"
            }, new String[]{
                    label
            }));
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Messages.OFFLINE_PLAYER);
            return true;
        }

        Integer amount = null;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception ex) {
            // ignore
        }

        if (amount == null || amount <= 0) {
            sender.sendMessage(Messages.INVALID_AMOUNT);
            return true;
        }

        ItemStack item = Thunder.getInstance().getItem();

        for (int i = 0; i < amount; ++i) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
                continue;
            }

            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
        }
        return false;
    }
}