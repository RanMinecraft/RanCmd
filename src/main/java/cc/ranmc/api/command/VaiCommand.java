package cc.ranmc.api.command;

import cc.ranmc.api.Main;
import cc.ranmc.api.util.BasicUtil;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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

public class VaiCommand implements CommandExecutor {
    
    private final static Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NonNull Command cmd,
                             @NotNull String label,
                             String @NonNull [] args) {


        if (!sender.hasPermission("roa.admin")) {
            sender.sendMessage(color("&a没有权限"));
            return true;
        }

        // 以下指令不能在控制台输入
        if (!(sender instanceof Player player)) {
            BasicUtil.print(color("&c该指令不能在控制台输入"));
            return true;
        }

        sender.sendMessage(color("&e修复村民 AI 开始运行"));
        Bukkit.getWorlds().forEach(world -> {
            for (Entity entity : world.getEntities()) {
                entity.getScheduler().run(Main.getInstance(), task -> {
                    if (entity instanceof Villager villager && !villager.hasAI()) {
                        villager.setAI(true);
                    }
                }, ()->{});
                sender.sendMessage(color("&a修复村民 AI 成功"));
            }
        });
        return true;
    }
}
