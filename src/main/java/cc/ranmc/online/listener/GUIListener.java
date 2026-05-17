package cc.ranmc.online.listener;

import cc.ranmc.online.Main;
import cc.ranmc.online.util.BasicUtil;
import cc.ranmc.online.util.CustomizeUtil;
import cc.ranmc.online.util.InputUtil;
import cc.ranmc.online.util.UpgradeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static cc.ranmc.online.util.BasicUtil.color;
import static cc.ranmc.online.util.BasicUtil.rgbString;

public class GUIListener implements Listener {

    private static final Main plugin = Main.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l领地宣传栏"))) {
            event.setCancelled(true);
            if (clicked == null) return;
            if (clicked.getType().toString().endsWith("_SIGN")) {
                player.closeInventory();
                String[] adInfo = plugin.getDataYml().getStringList("ad-list").get(event.getRawSlot()-1).split(" ");
                player.chat("/res tp " + adInfo[0]);
            }
            // 取消按钮
            if (event.getRawSlot() == 0) player.chat("/ad create");
        }

        // 修复耐久 GUI
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l创世之境丨修复物品"))) {
            // 取消点击
            if (event.getRawSlot() != 4 && inventory != player.getInventory()) {
                event.setCancelled(true);
            }
            if (clicked == null || inventory == null) return;
            // 确认按钮
            if (event.getRawSlot() == 6 && clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
                UpgradeUtil.fixAnimation(player, inventory);
            }
            // 取消按钮
            if (event.getRawSlot() == 2 && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
            }
        }

        // 强化物品 GUI
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l创世之境丨装备强化"))) {
            // 取消点击
            if (event.getRawSlot() != 4 && inventory != player.getInventory()) {
                event.setCancelled(true);
            }
            if (clicked == null || inventory == null) return;
            // 确认按钮
            if (event.getRawSlot() == 6 && clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
                UpgradeUtil.gradeAnimation(player, inventory);
            }
            // 关闭按钮
            if (event.getRawSlot() == 2 && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
            }
        }

        // 强化物品 GUI
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l创世之境丨快速强化"))) {
            // 取消点击
            if (event.getRawSlot() != 4 && inventory != player.getInventory()) {
                event.setCancelled(true);
            }
            if (clicked == null || inventory == null) return;
            // 确认按钮
            if (event.getRawSlot() == 6 && clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
                UpgradeUtil.quickGradeAnimation(player, inventory);
            }
            // 取消按钮
            if (event.getRawSlot() == 2 && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
            }
        }

        // 清除属性 GUI
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l创世之境丨清除属性"))) {
            // 取消点击
            if (event.getRawSlot() != 4 && inventory != player.getInventory()) {
                event.setCancelled(true);
            }
            if (clicked == null || inventory == null) return;
            // 确认按钮
            if (event.getRawSlot() == 6 && clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
                UpgradeUtil.clearAnimation(player, inventory);
            }
            // 取消按钮
            if (event.getRawSlot() == 2 && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
            }
        }

        // 定制物品名称
        if (event.getView().getTitle().equalsIgnoreCase(color("&b&l创世之境丨定制物品名称"))) {
            if (event.getRawSlot() != 4 && inventory != player.getInventory()) {
                event.setCancelled(true);
            }
            if (clicked == null || inventory == null) return;
            if ((event.getRawSlot() == 2 || event.getRawSlot() == 6) && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
            }
            if (event.getRawSlot() == 6 && clicked.getType() == Material.ANVIL) {
                int count = plugin.getDataYml().getInt("item-name.count." + player.getName());
                if (count <= 0) {
                    player.chat(plugin.getConfig().getString("item-name-command", "/mp buy 定制物品名称"));
                    return;
                } else {
                    ItemStack item = inventory.getItem(4);
                    if (item == null) {
                        player.sendMessage(color("&c请先放入物品"));
                        return;
                    }
                    if (Material.NAME_TAG == item.getType()) {
                        player.sendMessage(color("&c不能修改命名牌,请放入铁砧修改"));
                        return;
                    }
                    if (item.getAmount() != 1) {
                        player.sendMessage(color("&c物品数量必须为1"));
                        return;
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(rgbString(plugin.getDataYml().getString("item-namen.ame." + player.getName(), "&f物品名称")));
                    item.setItemMeta(meta);
                    count--;
                    plugin.getDataYml().set("item-name.count." + player.getName(), count);
                    try {
                        plugin.getDataYml().save(plugin.getDataFile());
                    } catch (IOException e) {
                        player.sendMessage(color("&c保存数据错误，请联系管理员：" + e.getMessage()));
                    }
                    player.sendMessage(color("&a更改成功,请取回物品"));
                    ItemStack pane = BasicUtil.getItem(Material.RED_STAINED_GLASS_PANE, 1, "&c关闭菜单");
                    inventory.setItem(2, pane);
                    inventory.setItem(6, pane);
                }

            }
            if (event.getRawSlot() == 2 && clicked.getType() == Material.OAK_SIGN) {
                InputUtil.open(player, "定制物品名称", context ->
                        plugin.getFoliaLib().getScheduler().runLater(() ->
                                CustomizeUtil.setItemName(player, context), 1));
            }
        }
    }

    //关闭背包事件
    private static final List<String> returnItemInvList = Arrays.asList(
            color("&b&l创世之境丨定制物品名称"),
            color("&b&l创世之境丨装备强化"),
            color("&b&l创世之境丨修复物品"),
            color("&b&l创世之境丨清除属性"),
            color("&b&l创世之境丨快速强化"));

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        //关闭菜单返还玩家物品
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (returnItemInvList.contains(event.getView().getTitle())) {
            ItemStack playerItem = inventory.getItem(4);
            if (playerItem != null) {
                if (BasicUtil.isInventoryFull(player)) {
                    player.getWorld().dropItem(player.getLocation(), playerItem);
                    player.sendMessage(color("&c背包已满，物品已掉落地面"));
                } else {
                    player.getInventory().addItem(playerItem);
                    inventory.remove(playerItem);
                }
            }
        }
    }
}
