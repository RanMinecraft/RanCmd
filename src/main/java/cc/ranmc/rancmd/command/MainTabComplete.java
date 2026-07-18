package cc.ranmc.rancmd.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String alias,
                                      String @NonNull [] args) {
        if (!sender.hasPermission("rancmd.admin")) return new ArrayList<>();
        if (args.length == 1) return Arrays.asList("reload", "help", "cmd", "opcmd");
        if (args.length == 2 && Arrays.asList("cmd", "opcmd").contains(args[0])) return null;
        if (args.length == 3 && Arrays.asList("cmd", "opcmd").contains(args[0])) return Collections.singletonList("指令(不带斜杠)");;
        return new ArrayList<>();
    }

}