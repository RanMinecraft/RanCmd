package cc.ranmc.online.command;

import cc.ranmc.online.Main;
import cc.ranmc.online.util.CustomizeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static cc.ranmc.online.util.BasicUtil.color;
import static cc.ranmc.online.util.BasicUtil.print;

public class NameCommand implements CommandExecutor {

    private final static Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             String @NotNull [] args) {
        if (args.length == 1) {
            if (sender.hasPermission("ranmc.admin")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(color("&c该玩家不在线"));
                    return true;
                }
                plugin.getDataYml().set("item-name.count." + player.getName(),
                        plugin.getDataYml().getInt("item-name.count." + player.getName(), 0) + 1);
                try {
                    plugin.getDataYml().save(plugin.getDataFile());
                } catch (IOException e) {
                    player.sendMessage(color("&c保存数据错误，请联系管理员：" + e.getMessage()));
                    return true;
                }
                player.sendMessage(color("&a你已经获得定制次数"));
                CustomizeUtil.openNameInventory(player);
                sender.sendMessage(color("&a已经成功发放定制次数"));
            } else {
                sender.sendMessage(color("&c权限不足"));
            }
            return true;
        }

        // 以下指令不能在控制台输入
        if (!(sender instanceof Player player)) {
            print(color("&c该指令不能在控制台输入"));
            return true;
        }
        if (args.length == 0) {
            CustomizeUtil.openNameInventory(player);
            return true;
        }
        sender.sendMessage(color("&c未知指令"));
        return true;
    }
}
