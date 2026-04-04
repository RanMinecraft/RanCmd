package cc.ranmc.api.listener;

import cc.ranmc.api.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static cc.ranmc.api.util.BasicUtil.color;

public class GUIListener implements Listener {

    private static final Main plugin = Main.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
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
    }
}
