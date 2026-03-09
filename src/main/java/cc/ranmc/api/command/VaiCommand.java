package cc.ranmc.api.command;

import cc.ranmc.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

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

        sender.sendMessage(color("&e修复村民 AI 开始运行"));
        Bukkit.getWorlds().forEach(world -> {
            AtomicInteger count = new AtomicInteger();
            for (Entity entity : world.getEntities()) {
                entity.getScheduler().run(Main.getInstance(), task -> {
                    if (entity instanceof Villager villager && !villager.hasAI()) {
                        villager.setAI(true);
                        count.getAndIncrement();
                    }
                }, ()->{});
            }
            sender.sendMessage(color("&a成功修复村民 AI : " + count + "个"));
        });
        return true;
    }
}
