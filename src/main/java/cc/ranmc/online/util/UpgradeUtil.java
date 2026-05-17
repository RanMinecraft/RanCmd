package cc.ranmc.online.util;

import cc.ranmc.online.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cc.ranmc.online.util.AttributeUtil.ATTRIBUTES;
import static cc.ranmc.online.util.BasicUtil.color;

public class UpgradeUtil {
    private static final Main plugin = Main.getInstance();
    private static final ItemStack PANE = BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, "&7请放入物品");
    private static final String PREFIX = color("&b[创世之境]");
    public static final String UPGRADE_PREFIX = "§c[§b装备强化§c]§a";
    
    /**
     * 修复物品属性
     * @param player 玩家
     * @param item 物品
     */
    public static void clearItem(Player player, ItemStack item) {
        if (item == null) {
            player.sendMessage(color("&c请先放入物品"));
            return;
        }
        ItemMeta meta = item.getItemMeta();
        double money = plugin.getEcon().getBalance(player);
        double price = plugin.getConfig().getDouble("upgrade-clear-price");
        List<String> lores =  item.getItemMeta().getLore();

        if (lores == null || lores.getFirst().contains(UPGRADE_PREFIX + " 0 / ")) {
            player.sendMessage(color("&c该物品没有任何属性"));
            return;
        }
        if (item.getAmount() != 1) {
            player.sendMessage(color("&c物品数量必须为1"));
            return;
        }
        if (money < price) {
            player.sendMessage(color("&c你没有足够的金币"));
            return;
        }
        plugin.getEcon().withdrawPlayer(player, price);
        List<String> newLores = new ArrayList<>();
        for (String lore : lores) {
            if (lore.contains(UPGRADE_PREFIX)) newLores.add(UPGRADE_PREFIX + " 0 / " + lore.split(" ")[3]);
        }
        meta.setLore(newLores);
        item.setItemMeta(meta);
        player.sendMessage(color("&a清除成功,请取回物品"));
    }

    /**
     * 修复物品
     */
    public static void fixItem(Player player, ItemStack item) {
        if (item == null) {
            player.sendMessage(color("&c请先放入物品"));
            return;
        }
        ItemMeta meta = item.getItemMeta();
        double money = plugin.getEcon().getBalance(player);
        double price = plugin.getConfig().getDouble("fix-item-price");
        if (player.hasPermission("roa.vip")) price = 0;
        List<String> lore =  meta.getLore();
        if (item.getAmount() != 1) {
            player.sendMessage(color("&c物品数量必须为1"));
            return;
        }
        if (money < price) {
            player.sendMessage(color("&c你没有足够的金币"));
            return;
        }
        plugin.getEcon().withdrawPlayer(player, price);
        if (lore != null) {
            for (int ii = 0; ii < lore.size(); ii++) {
                String ss = lore.get(ii);
                if (ss.contains("耐久度")) {
                    lore.set(ii, color("&e&l耐久度:"+ plugin.getConfig().getInt("FixDurability")));
                }
            }
        }
        meta.setLore(lore);
        ((Damageable) meta).setDamage(0);
        item.setItemMeta(meta);
        player.sendMessage(color("&a修复成功,请取回物品"));
    }

    /**
     * 修复动画
     */
    public static void fixAnimation(Player player, Inventory inventory) {
        inventory.setItem(0, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(1, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(3, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(5, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(6, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(7, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(8, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, " "));
        plugin.getFoliaLib().getScheduler().runLater(() ->
                inventory.setItem(1, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ")),5);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(8, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(7, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }, 10);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(0, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(5, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }, 15);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(3, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE,
                    1,
                    "&b取消修复",
                    "&9点击关闭菜单"));
            inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE,
                    1,
                    "&b确认修复",
                    "&e花费金币: " + plugin.getConfig().getInt("fix-item-price",2000),
                    "&9VIP免费使用",
                    "&9修复物品耐久度"));
            ItemStack item = inventory.getItem(4);
            if (item == null) {
                player.sendMessage(color("&c请先放入物品"));
            } else {
                fixItem(player, item);
            }
        }, 20);
    }

    /**
     * 清除动画
     */
    public static void clearAnimation(Player player, Inventory inventory) {
        inventory.setItem(2, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(6, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(3, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        inventory.setItem(5, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(3, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(5, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(6, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 5);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(2, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(6, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(1, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(7, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 10);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(1, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(7, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(0, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(8, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 15);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(0, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(8, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE,
                    1,
                    "&b取消清除",
                    "&9点击关闭菜单"));
            inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE,
                    1,
                    "&b确认清除",
                    "&e花费金币: " + plugin.getConfig().getDouble("upgrade-clear-price"),
                    "&9清除物品全部属性",
                    "&9不影响物品的附魔",
                    "&9请慎重使用该功能"));
            ItemStack item = inventory.getItem(4);
            if (item == null) {
                player.sendMessage(color("&c请先放入物品"));
            } else {
                clearItem(player, item);
            }
        }, 20);
    }

    /**
     * 强化动画
     */
    public static void gradeAnimation(Player player, Inventory inventory) {
        inventory.setItem(2, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(6, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

        inventory.setItem(0, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        inventory.setItem(8, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(0, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(8, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(1, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(7, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 5);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(1, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(7, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(6, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 10);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(2, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(6, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(3, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(5, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 15);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(3, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(5, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE,
                    1,
                    "&b取消强化",
                    "&9点击关闭菜单"));
            inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE,
                    1,
                    "&b确认强化",
                    "&e花费金币: " + plugin.getConfig().getDouble("upgrade-price"),
                    "&9请放置强化物品",
                    "&9支持全物品强化",
                    "&9将获得随机属性"));
            ItemStack item = inventory.getItem(4);
            if (item == null) {
                player.sendMessage(color("&c请先放入物品"));
            } else {
                gradeItem(player, item, false);
            }
        }, 20);
    }

    /**
     * 强化动画
     */
    public static void quickGradeAnimation(Player player, Inventory inventory) {
        inventory.setItem(2, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        inventory.setItem(6, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

        inventory.setItem(0, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        inventory.setItem(8, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(1, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(7, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 5);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(2, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(6, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 10);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(3, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
            inventory.setItem(5, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }, 15);

        plugin.getFoliaLib().getScheduler().runLater(() -> {
            inventory.setItem(0, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(8, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(1, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(7, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(3, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(5, BasicUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE,
                    1,
                    "&b取消强化",
                    "&9点击关闭菜单"));
            inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE,
                    1,
                    "&b确认强化至最大",
                    "&e花费金币/次: " + plugin.getConfig().getDouble("upgrade-price"),
                    "&9请放置强化物品",
                    "&9支持全物品强化",
                    "&9将获得随机属性"));
            ItemStack item = inventory.getItem(4);
            if (item == null) {
                player.sendMessage(color("&c请先放入物品"));
            } else {
                gradeItem(player, item, true);
            }
        }, 20);
    }

    /**
     * 强化物品
     */
    public static void gradeItem(Player player, ItemStack item, boolean quick) {
        ItemMeta meta = item.getItemMeta();
        double money = plugin.getEcon().getBalance(player);
        double price = plugin.getConfig().getDouble("upgrade-price");
        List<String> lores =  meta.getLore();

        int info = 0;
        if (lores == null) {
            lores = new ArrayList<>();
            lores.add(UPGRADE_PREFIX + " 0 / "+ plugin.getConfig().getInt("upgrade-count"));
        } else {
            boolean cannot = true;
            for (int i = 0; i < lores.size(); i++) {
                if (lores.get(i).contains(UPGRADE_PREFIX)) {
                    info = i;
                    cannot = false;
                    break;
                }
            }
            if (cannot) {
                player.sendMessage(color("&c你不能强化该物品"));
                return;
            }
        }
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            player.sendMessage(color("&c书籍不能被强化"));
            return;
        }
        if (item.getType() == Material.FILLED_MAP || item.getType() == Material.MAP) {
            player.sendMessage(color("&c地图不能被强化"));
            return;
        }
        if (item.getType().toString().contains("BANNER")) {
            player.sendMessage(color("&c旗帜不能被强化"));
            return;
        }
        if (item.getAmount() != 1) {
            player.sendMessage(color("&c物品数量必须为1"));
            return;
        }
        int gradeNum = Integer.parseInt(lores.get(info).split(" ")[1]);
        int maxNum = Integer.parseInt(lores.get(info).split(" ")[3]);
        if (gradeNum >= plugin.getConfig().getInt("max-upgrade-count")) {
            player.sendMessage(color("&c已达最大强化上限:" + plugin.getConfig().getInt("max-upgrade-count")));
            return;
        }
        int count = 1;
        if (quick) {
            count = maxNum - gradeNum;
        }
        price *= count;
        if (money < price) {
            player.sendMessage(color("&c你没有足够的金币"));
            return;
        }
        if (!quick && gradeNum >= maxNum) {
            if (TearUtil.cost(player)) {
                maxNum++;
            } else {
                player.sendMessage(color("&c你没有持有龙泪"));
                return;
            }
        }
        plugin.getEcon().withdrawPlayer(player, price);
        gradeNum += count;
        lores.set(info, UPGRADE_PREFIX + " " + gradeNum + " / " + maxNum);
        while (count > 0) {
            count--;
            lores.add(color("&e" + generateAttribute()));
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        player.sendMessage(color("&a" + (quick ? "快速" : "") + "强化成功,请取回物品"));
    }

    /**
     * 生成新强化属性
     * @return 强化属性
     */
    public static String generateAttribute() {
        int value = new Random().nextInt(8) - 2;
        if (value <= 0) value += 3;
        return ATTRIBUTES.get(new Random().nextInt(ATTRIBUTES.size())) + ": " + value;
    }

    /**
     * 打开修复耐久菜单
     */
    public static void openFixInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§b§l创世之境丨修复物品");
        inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE, 1,
                "&b确认修复",
                "&e花费金币: " + plugin.getConfig().getInt("fix-item-price",2000),
                "&9VIP免费使用",
                "&9修复物品耐久度"));
        inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, "&c关闭菜单"));
        inventory.setItem(0, PANE);
        inventory.setItem(1, PANE);
        inventory.setItem(3, PANE);
        inventory.setItem(5, PANE);
        inventory.setItem(7, PANE);
        inventory.setItem(8, PANE);

        player.openInventory(inventory);
    }

    /**
     * 打开清除属性菜单
     */
    public static void openClearInv(Player player, int price) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§b§l创世之境丨清除属性");
        inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE, 1,
                "&b确认清除",
                "&e花费金币: " + price,
                "&9清除物品全部属性",
                "&9不影响物品的附魔",
                "&9请慎重使用该功能"));
        inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, "&c关闭菜单"));
        inventory.setItem(0, PANE);
        inventory.setItem(1, PANE);
        inventory.setItem(3, PANE);
        inventory.setItem(5, PANE);
        inventory.setItem(7, PANE);
        inventory.setItem(8, PANE);
        player.openInventory(inventory);
    }

    /**
     * 打开强化属性菜单
     */
    public static void openGradeInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§b§l创世之境丨装备强化");
        inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE, 1,
                "&b确认强化",
                "&e花费金币: " + plugin.getConfig().getInt("upgrade-price",2000),
                "&9请放置强化物品",
                "&9支持全物品强化",
                "&9将获得随机属性"));
        inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, "&c关闭菜单"));
        inventory.setItem(0, PANE);
        inventory.setItem(1, PANE);
        inventory.setItem(3, PANE);
        inventory.setItem(5, PANE);
        inventory.setItem(7, PANE);
        inventory.setItem(8, PANE);
        player.openInventory(inventory);
    }

    /**
     * 打开强化属性菜单
     */
    public static void openQuickGradeInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§b§l创世之境丨快速强化");
        inventory.setItem(6, BasicUtil.getItem(Material.LIME_STAINED_GLASS_PANE, 1,
                "&b确认强化至最大",
                "&e花费金币/次: " + plugin.getConfig().getInt("upgrade-price",2000),
                "&9请放置强化物品",
                "&9支持全物品强化",
                "&9将获得随机属性"));
        inventory.setItem(2, BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, "&c关闭菜单"));
        inventory.setItem(0, PANE);
        inventory.setItem(1, PANE);
        inventory.setItem(3, PANE);
        inventory.setItem(5, PANE);
        inventory.setItem(7, PANE);
        inventory.setItem(8, PANE);
        player.openInventory(inventory);
    }
}
