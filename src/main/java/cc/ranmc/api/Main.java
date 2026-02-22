package cc.ranmc.api;

import cc.ranmc.api.command.MainCommand;
import cc.ranmc.api.command.MainTabComplete;
import cc.ranmc.api.util.BasicUtil;
import com.alibaba.fastjson2.JSONObject;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static cc.ranmc.api.util.FoliaUtil.isFolia;

public class Main extends JavaPlugin implements Listener {

    private static JSONObject tps = new JSONObject();

    @Override
    public void onEnable() {
        Javalin.create()
                .get("/tps", this::getTps)
                .get("/online", this::getOnline)
                .start(2263);

        // 注册指令
        PluginCommand mainCmd = Bukkit.getPluginCommand("roa");
        mainCmd.setExecutor(new MainCommand());
        mainCmd.setTabCompleter(new MainTabComplete());

        Bukkit.getPluginManager().registerEvents(this, this);

        tps.put("code", 200);
        tps.put("data", new ArrayList<>());
        if (isFolia()) {
            Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(this,
                    task -> logTps(), 0, 20 * 60 * 10);
        } else {
            Bukkit.getServer().getScheduler().runTaskTimer(this, this::logTps, 0, 20 * 60 * 10);
        }
        super.onEnable();
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Item item) {
            event.setCancelled(true);
            item.remove();
        }
    }

    public void logTps() {
        ArrayList<Object> data = tps.getJSONArray("data");
        JSONObject json = new JSONObject();
        json.put("Date", BasicUtil.getDate(LocalDateTime.now()));
        json.put("Time", BasicUtil.getTime());
        json.put("Player", Bukkit.getOnlinePlayers().size());
        json.put("Value", Math.min(20, Bukkit.getServer().getTPS()[0]));
        data.addFirst(json);
        if (data.size() > 72) data.removeLast();
        tps.put("data", data);
    }

    public void getTps(Context context) {
        context.contentType(ContentType.APPLICATION_JSON);
        context.result(tps.toString());
    }

    public void getOnline(Context context) {
        JSONObject res = new JSONObject();
        res.put("code", 200);
        JSONObject data = new JSONObject();
        data.put("online", Bukkit.getOnlinePlayers().size());
        data.put("max", Bukkit.getMaxPlayers());
        data.put("version", Bukkit.getBukkitVersion().split("-")[0]);
        res.put("data", data);
        context.contentType(ContentType.APPLICATION_JSON);
        context.result(res.toString());
    }

}
