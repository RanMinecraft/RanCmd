package cc.ranmc.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AdTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String alias,
                                      String @NonNull [] args) {
        if (args.length == 1) return List.of("领地名");
        if (args.length == 2) return List.of("介绍(\\n换行)");
        return new ArrayList<>();
    }

}