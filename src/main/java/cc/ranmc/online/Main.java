package cc.ranmc.online;

import cc.ranmc.online.command.AdCommand;
import cc.ranmc.online.command.AdTabComplete;
import cc.ranmc.online.command.AttributeCommand;
import cc.ranmc.online.command.AttributeTabComplete;
import cc.ranmc.online.command.MainCommand;
import cc.ranmc.online.command.MainTabComplete;
import cc.ranmc.online.command.VaiCommand;
import cc.ranmc.online.listener.AttributeListener;
import cc.ranmc.online.listener.GUIListener;
import cc.ranmc.online.util.BasicUtil;
import cc.ranmc.online.util.TearUtil;
import com.alibaba.fastjson2.JSONObject;
import com.tcoded.folialib.FoliaLib;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

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

        PluginCommand vaiCmd = Bukkit.getPluginCommand("vai");
        Objects.requireNonNull(vaiCmd).setExecutor(new VaiCommand());

        PluginCommand attributeCmd = Bukkit.getPluginCommand("attribute");
        Objects.requireNonNull(attributeCmd).setExecutor(new AttributeCommand());
        attributeCmd.setTabCompleter(new AttributeTabComplete());

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new AttributeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

        tps.put("code", 200);
        tps.put("data", new ArrayList<>());
        foliaLib.getScheduler().runTimer(this::logTps, 1, 20 * 60 * 10);

        super.onEnable();
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Creeper creeper &&
                event.getEntity() instanceof Player player) {
            ItemStack item = player.getInventory().getHelmet();
            if (item != null && item.getType() == Material.PLAYER_HEAD && creeper.isPowered()) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                if (!Objects.requireNonNull(meta).hasOwner()) {
                    meta.setOwningPlayer(player);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Location location = event.getEntity().getLocation();
            Objects.requireNonNull(location.getWorld()).dropItem(location, TearUtil.getTearItem()).setGlowing(true);
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
