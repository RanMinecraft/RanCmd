package cc.ranmc.online.command;


import cc.ranmc.online.util.BasicUtil;
import cc.ranmc.online.util.InputUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.ranmc.online.util.BasicUtil.color;

public class CancelInputCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             String[] args) {
        // 以下指令不能在控制台输入
        if (!(sender instanceof Player player)) {
            BasicUtil.print(color("&c该指令不能在控制台输入"));
            return true;
        }

        if (InputUtil.getInputMap().containsKey(player.getName())) {
            InputUtil.getInputMap().remove(player.getName());
            player.sendMessage(color("&a你已取消输入状态"));
        } else {
            player.sendMessage(color("&c当前不是输入状态"));
        }
        return true;
    }
}