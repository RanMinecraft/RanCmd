package cc.ranmc.online.util;

import cc.ranmc.online.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cc.ranmc.online.util.BasicUtil.clearColor;
import static cc.ranmc.online.util.BasicUtil.color;
import static cc.ranmc.online.util.BasicUtil.rgbString;

public class CustomizeUtil {

    private static final Main plugin = Main.getInstance();

    public static void setItemName(Player player, String name) {
        name = name.toLowerCase().replace(" ", "")
                .replace("§", "")
                .replace("&k", "")
                .replace("&m", "")
                .replace("&n", "")
                .replace("&o", "")
                .replace("&r", "");
        if (name.isEmpty()) {
            player.sendMessage(color("&c你没有输入任何内容"));
            return;
        }
        if (clearColor(name).length() > plugin.getConfig().getInt("item-name-max-length", 6)) {
            player.sendMessage(color("&c你输入的名称不规范或过长"));
            return;
        }
        plugin.getDataYml().set("itemname.name." + player.getName(), name);
        try {
            plugin.getDataYml().save(plugin.getDataFile());
        } catch (IOException e) {
            player.sendMessage(color("&c保存数据错误，请联系管理员：" + e.getMessage()));
        }
        openNameInventory(player);
    }

    public static void openNameInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, color("&b&l创世之境丨定制物品名称"));
        ItemStack item1 = new ItemStack(Material.ANVIL);
        ItemMeta meta = item1.getItemMeta();
        meta.setDisplayName(color("&c确认更改"));
        List<String> lore = new ArrayList<>();
        lore.add(color("&b拥有修改次数 &e" + plugin.getConfig().getInt("itemname.count." + player.getName(), 0)));
        lore.add(color("&b价格： &a" + plugin.getConfig().getInt("item-name-price", 20) + " &b元/次"));
        lore.add(color("&b点击确认更改或打开充值"));
        meta.setLore(lore);
        item1.setItemMeta(meta);

        ItemStack item5 = new ItemStack(Material.OAK_SIGN);
        ItemMeta meta5 = item5.getItemMeta();
        meta5.setDisplayName(rgbString(plugin.getDataYml().getString("itemname.name." + player.getName(), "&f物品名称")));
        List<String> lore5 = new ArrayList<>();
        lore5.add(color("&e点击输入物品名称"));
        meta5.setLore(addColorLore(lore5));
        item5.setItemMeta(meta5);

        ItemStack pane = BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ");

        inventory.setItem(0, pane);
        inventory.setItem(1, pane);
        inventory.setItem(3, pane);
        inventory.setItem(5, pane);
        inventory.setItem(7, pane);
        inventory.setItem(8, pane);
        inventory.setItem(2, item5);
        inventory.setItem(6, item1);
        player.openInventory(inventory);
    }

    public static List<String> addColorLore(List<String> lore) {
        lore.add(color("&9颜色符号实例"));
        lore.add("§f&a亮绿->"+ color("&a亮绿")+"  §f&b亮蓝->"+ color("&b亮蓝"));
        lore.add("§f&c红色->"+ color("&c红色")+"  §f&d粉色->"+ color("&d粉色"));
        lore.add("§f&e黄色->"+ color("&e黄色")+"  §f&f白色->"+ color("&f白色"));
        lore.add("§f&0黑色->"+ color("&0黑色")+"  §f&1蓝色->"+ color("&1蓝色"));
        lore.add("§f&2绿色->"+ color("&2绿色")+"  §f&3青色->"+ color("&3青色"));
        lore.add("§f&4深红->"+ color("&4深红")+"  §f&5紫色->"+ color("&5紫色"));
        lore.add("§f&6金色->"+ color("&6金色")+"  §f&7浅灰->"+ color("&7浅灰"));
        lore.add("§f&8深灰->"+ color("&8深灰")+"  §f&9浅蓝->"+ color("&9浅蓝"));
        lore.add("§f&#2196f3->"+ rgbString("&#2196f3 支持6位RGB颜色代码"));
        return lore;
    }
}
