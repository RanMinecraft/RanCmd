package cc.ranmc.online.listener;

import cc.ranmc.online.Main;
import cc.ranmc.online.util.UpgradeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static cc.ranmc.online.util.BasicUtil.color;

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
                String[] adInfo = plugin.getConfig().getStringList("ad-list").get(event.getRawSlot()-1).split(" ");
                player.chat("/res tp " + adInfo[0]);
            }
            // 取消按钮
            if (event.getRawSlot() == 0) {
                player.closeInventory();
            }
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
    }
}
