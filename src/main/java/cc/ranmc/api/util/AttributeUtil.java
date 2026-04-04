package cc.ranmc.api.util;

import cc.ranmc.api.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeUtil {

    private List<String> attributes = List.of("");

    /**
     * 获取玩家属性信息
     * @param player 玩家
     * @return 属性信息
     */
    public static Map<String,Integer> getMetaLoreAP(Player player) {
        Main plugin = Main.getInstance();
        PlayerInventory inventory = player.getInventory();
        List<ItemMeta> metaList = getItemMetaList(
                inventory.getItemInMainHand(),
                inventory.getItemInOffHand(),
                inventory.getHelmet(),
                inventory.getChestplate(),
                inventory.getLeggings(),
                inventory.getBoots());
        Map<String,Integer> info = new HashMap<>();
        for (ItemMeta meta : metaList) {
            List<String> list = meta.getLore();
            List<String> attributes = plugin.getConfig().getStringList("RandomGrade");
            attributes.addAll(plugin.getConfig().getStringList("HideGrade"));
            if (list != null) {
                for (String lore : list) {
                    for (String name : attributes) {
                        int value = info.getOrDefault(name, 0) + getLoreValue(lore, name);
                        info.put(name, name.contains("几率") ? Math.min(value, 50) : value);
                    }
                }
            }
        }
        return info;
    }

    private static List<ItemMeta> getItemMetaList(ItemStack... items) {
        List<ItemMeta> metas = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null &&
                    item.getType() != Material.FILLED_MAP &&
                    item.getType() != Material.WRITTEN_BOOK &&
                    item.getType() != Material.WRITABLE_BOOK &&
                    !item.getType().toString().contains("BANNER")) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) metas.add(meta);
            }
        }
        return metas;
    }

    /**
     * 获取属性点数
     * @param lore 文本内容
     * @param name 属性名字
     * @return 属性点数
     */
    private static int getLoreValue(String lore, String name) {
        int value = 0;
        if (lore.contains(name)) {
            try {
                value += (int) Double.parseDouble(ChatColor.stripColor(lore)
                        .replace(name, "")
                        .replace(" ", "")
                        .replace(":", "")
                        .replace("：", ""));
            } catch (NumberFormatException ignored) {
            }
        }
        return value;
    }
}
