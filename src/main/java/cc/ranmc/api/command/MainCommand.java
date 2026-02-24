package cc.ranmc.api.command;

import cc.ranmc.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import static cc.ranmc.api.util.BasicUtil.color;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NonNull Command cmd,
                             @NotNull String label,
                             String @NonNull [] args) {


        if (!sender.hasPermission("roa.admin")) {
            sender.sendMessage(color("&a没有权限"));
            return true;
        }

        if (args.length == 0) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(color("&a插件重载成功"));
            return true;
        }
        
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(color("&c该玩家不在线"));
                return true;
            }
            PlayerInventory inventory = target.getInventory();
            if (inventory.getHelmet() != null) inventory.getHelmet().removeEnchantment(Enchantment.BINDING_CURSE);
            if (inventory.getChestplate() != null) inventory.getChestplate().removeEnchantment(Enchantment.BINDING_CURSE);
            if (inventory.getLeggings() != null) inventory.getLeggings().removeEnchantment(Enchantment.BINDING_CURSE);
            if (inventory.getBoots() != null) inventory.getBoots().removeEnchantment(Enchantment.BINDING_CURSE);
            return true;
        }

        sender.sendMessage(color("&c未知指令"));
        return true;
    }
}
