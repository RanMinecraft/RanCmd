package cc.ranmc.rancmd.command;

import cc.ranmc.rancmd.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import static cc.ranmc.rancmd.Main.PREFIX;
import static cc.ranmc.rancmd.util.BasicUtil.color;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NonNull Command cmd,
                             @NotNull String label,
                             String @NonNull [] args) {


        if (!sender.hasPermission("rancmd.admin")) {
            sender.sendMessage(PREFIX + color("&a没有权限"));
            return true;
        }
        
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Main.getInstance().loadConfig();
                sender.sendMessage(PREFIX + color("&a重载成功"));
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(PREFIX + color(
                        "&a感谢你的使用&e\n" +
                                "/rancmd help 插件帮助\n" +
                                "/rancmd reload 重载插件\n" +
                                "/rancmd cmd <玩家名> <指令> 使玩家执行指令\n" +
                                "/rancmd opcmd <玩家名> <指令> 使玩家以op执行指令"));
                return true;
            }
        }

        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("cmd")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(PREFIX + color("&c该玩家不在线"));
                    return true;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
                        Bukkit.dispatchCommand(target, builder.toString()));
                sender.sendMessage(PREFIX + color("&a执行成功"));
                return true;
            }
            if (args[0].equalsIgnoreCase("opcmd")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(PREFIX + color("&c该玩家不在线"));
                    return true;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    boolean op = target.isOp();
                    if (!op) target.setOp(true);
                    Bukkit.dispatchCommand(target, builder.toString());
                    target.setOp(op);
                });
                sender.sendMessage(PREFIX + color("&a执行成功"));
                return true;
            }
        }

        /*if (!(sender instanceof Player)) {
            print("&c该指令不能在控制台输入");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("rancmd")) {
            if (!sender.hasPermission("rancmd.user")) {
                sender.sendMessage(PREFIX + color("&c没有权限"));
                return true;
            }
        }*/

        sender.sendMessage(PREFIX + color("&c未知指令"));
        return true;
    }
}
