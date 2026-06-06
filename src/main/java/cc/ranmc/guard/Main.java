package cc.ranmc.guard;

import cc.ranmc.guard.util.BasicUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static cc.ranmc.guard.util.BasicUtil.print;

public class Main extends JavaPlugin implements Listener {

    private static final String PLUGIN = "RanmcSecurity";
    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        print("&e-----------------------");
        print("&b" + PLUGIN + " &dBy阿然");
        print("&b插件版本:" + getPluginMeta().getVersion());
        print("&b服务器版本:" + Bukkit.getVersion());
        print("&chttps://www.ranmc.cc/");
        print("&e-----------------------");

        if (BasicUtil.isFolia()) {
            Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(this,
                    _ -> checkRanmc(), 100, 20);
        } else {
            Bukkit.getScheduler().runTaskTimer(this, this::checkRanmc, 100, 20);
        }

        super.onEnable();
    }

    private void checkRanmc() {
        Plugin ranmc = Bukkit.getPluginManager().getPlugin("Ranmc");
        if (ranmc != null && !ranmc.isEnabled()) {
            Bukkit.getOperators().forEach(player ->
                    BasicUtil.run("deop " + player.getName()));
            if (!Bukkit.hasWhitelist()) Bukkit.setWhitelist(true);
            Bukkit.getOnlinePlayers().forEach(Player::kick);
        }
    }
}
