package cc.ranmc.api;

import cc.ranmc.api.command.AdCommand;
import cc.ranmc.api.command.AdTabComplete;
import cc.ranmc.api.command.MainCommand;
import cc.ranmc.api.command.MainTabComplete;
import cc.ranmc.api.util.BasicUtil;
import com.alibaba.fastjson2.JSONObject;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static cc.ranmc.api.util.BasicUtil.color;
import static cc.ranmc.api.util.FoliaUtil.isFolia;

public class Main extends JavaPlugin implements Listener {

    private static final JSONObject tps = new JSONObject();
    private static Javalin javalin;
    @Getter
    private static Main instance;

    //经济插件
    @Getter
    private static Economy econ;

    @Override
    public void onEnable() {
        instance = this;

        javalin = Javalin.create()
                .get("/tps", this::getTps)
                .get("/online", this::getOnline)
                .start(2263);

        // 加载 config
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();

        econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        // 注册指令
        PluginCommand mainCmd = Bukkit.getPluginCommand("roa");
        Objects.requireNonNull(mainCmd).setExecutor(new MainCommand());
        mainCmd.setTabCompleter(new MainTabComplete());

        PluginCommand adCmd = Bukkit.getPluginCommand("ad");
        Objects.requireNonNull(adCmd).setExecutor(new AdCommand());
        adCmd.setTabCompleter(new AdTabComplete());

        Bukkit.getPluginManager().registerEvents(this, this);

        tps.put("code", 200);
        tps.put("data", new ArrayList<>());
        if (isFolia()) {
            Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(this,
                    task -> logTps(), 1, 20 * 60 * 10);
        } else {
            Bukkit.getServer().getScheduler().runTaskTimer(this, this::logTps, 0, 20 * 60 * 10);
        }
        super.onEnable();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l领地宣传栏"))) {
            event.setCancelled(true);
            if (clicked == null) return;
            if (clicked.getType().toString().endsWith("_SIGN")) {
                player.closeInventory();
                String[] adInfo = getConfig().getStringList("ad-list").get(event.getRawSlot()-1).split(" ");
                player.chat("/res tp " + adInfo[0]);
            }
            // 取消按钮
            if (event.getRawSlot() == 0) {
                player.closeInventory();
            }
        }

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

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            Player player = event.getPlayer();
            if (player.hasPermission("thy.joinfullserver")) {
                event.allow();
            }
        }
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
