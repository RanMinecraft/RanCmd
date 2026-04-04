package cc.ranmc.api.command;

import cc.ranmc.api.Main;
import cc.ranmc.api.util.AttributeUtil;
import cc.ranmc.api.util.BasicUtil;
import cc.ranmc.api.util.UpgradeUtil;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cc.ranmc.api.util.BasicUtil.color;

public class AttributeCommand implements CommandExecutor {
    
    private final static Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NonNull Command cmd,
                             @NotNull String label,
                             String @NonNull [] args) {


        if (!sender.hasPermission("roa.user")) {
            sender.sendMessage(color("&a没有权限"));
            return true;
        }

        // 以下指令不能在控制台输入
        if (!(sender instanceof Player player)) {
            BasicUtil.print(color("&c该指令不能在控制台输入"));
            return true;
        }

        // 修复耐久度
        if (args[0].equalsIgnoreCase("fix")) {
            UpgradeUtil.openFixInv(player);
            return true;
        }

        // 清除强化
        if (args[0].equalsIgnoreCase("clear")) {
            UpgradeUtil.openClearInv(player, plugin.getConfig().getInt("upgrade-clear-price",2000));
            return true;
        }

        // 属性强化
        if (args[0].equalsIgnoreCase("upgrade")) {
            UpgradeUtil.openGradeInv(player);
            return true;
        }

        // 快速强化
        if (args[0].equalsIgnoreCase("quick")) {
            UpgradeUtil.openQuickGradeInv(player);
            return true;
        }

        // 获取玩家属性信息
        if (args[0].equalsIgnoreCase("stats")) {
            AttributeUtil.openMetaBook(player);
            return true;
        }
        return true;
    }
}
