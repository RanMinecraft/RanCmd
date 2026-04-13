package cc.ranmc.online;

import cc.ranmc.online.command.AdCommand;
import cc.ranmc.online.command.AdTabComplete;
import cc.ranmc.online.command.AttributeCommand;
import cc.ranmc.online.command.AttributeTabComplete;
import cc.ranmc.online.command.MainCommand;
import cc.ranmc.online.command.MainTabComplete;
import cc.ranmc.online.command.VaiCommand;
import cc.ranmc.online.listener.AttributeListener;
import cc.ranmc.online.listener.EntityListener;
import cc.ranmc.online.listener.GUIListener;
import cc.ranmc.online.listener.PlayerListener;
import cc.ranmc.online.util.BasicUtil;
import com.alibaba.fastjson2.JSONObject;
import com.tcoded.folialib.FoliaLib;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static cc.ranmc.online.util.BasicUtil.print;

public class Main extends JavaPlugin {

    private static final JSONObject tps = new JSONObject();
    private static Javalin javalin;
    @Getter
    private static Main instance;
    //经济插件
    @Getter
    private Economy econ;
    @Getter
    private FoliaLib foliaLib;

    @Override
    public void onEnable() {
        instance = this;
        foliaLib = new FoliaLib(this);
        try {
            javalin = Javalin.create()
                    .get("/tps", this::getTps)
                    .get("/online", this::getOnline)
                    .start(2263);
        } catch (Exception ignored) {
            print("&ctps服务器启动失败");
        }

        // 加载 config
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            print("&c无法获取到经济插件");
        } else econ = rsp.getProvider();

        // 注册指令
        PluginCommand mainCmd = Bukkit.getPluginCommand("roa");
        Objects.requireNonNull(mainCmd).setExecutor(new MainCommand());
        mainCmd.setTabCompleter(new MainTabComplete());

        PluginCommand adCmd = Bukkit.getPluginCommand("ad");
        Objects.requireNonNull(adCmd).setExecutor(new AdCommand());
        adCmd.setTabCompleter(new AdTabComplete());

        PluginCommand vaiCmd = Bukkit.getPluginCommand("vai");
        Objects.requireNonNull(vaiCmd).setExecutor(new VaiCommand());

        PluginCommand attributeCmd = Bukkit.getPluginCommand("attribute");
        Objects.requireNonNull(attributeCmd).setExecutor(new AttributeCommand());
        attributeCmd.setTabCompleter(new AttributeTabComplete());

        Bukkit.getPluginManager().registerEvents(new AttributeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

        tps.put("code", 200);
        tps.put("data", new ArrayList<>());
        foliaLib.getScheduler().runTimer(this::logTps, 1, 20 * 60 * 10);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (javalin != null) {
            try {
                javalin.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        data.put("version", Bukkit.getBukkitVersion().split("-")[0].split("\\.build")[0]);
        res.put("data", data);
        context.contentType(ContentType.APPLICATION_JSON);
        context.result(res.toString());
    }

}
