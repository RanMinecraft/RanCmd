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

    public static final List<String> ATTRIBUTES = List.of(
            "真实伤害",
            "物理伤害",
            "物理防御",
            "暴击几率",
            "暴击伤害",
            "暴击防御",
            "吸血几率",
            "吸血伤害",
            "吸血防御",
            "冰冻几率",
            "流血几率",
            "致盲几率",
            "闪避几率",
            "漂浮几率",
            "反弹几率",
            "斩杀几率",
            "远程伤害",
            "近战伤害",
            "远程防御",
            "近战防御",
            "眩晕几率",
            "雷击几率",
            "虚弱几率",
            "击飞几率",
            "混乱几率",
            "雷击伤害");

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
            if (list != null) {
                for (String lore : list) {
                    for (String name : ATTRIBUTES) {
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

    /**
     * 打开玩家属性书
     * @param player 玩家
     */
    public static void openMetaBook(Player player) {
        Map<String,Integer> meta = getMetaLoreAP(player);
        StringBuilder builder = new StringBuilder("    "+player.getName()+"个人属性\n\n");
        List<String> text = new ArrayList<>();
        int count = 2;
        for (String name : ATTRIBUTES) {
            builder.append("   ");
            builder.append(name);
            builder.append(" - ");
            builder.append(meta.getOrDefault(name, 0));
            builder.append("\n");
            count++;
            if (count > 13) {
                text.add(builder.toString());
                builder = new StringBuilder();
                count = 0;
            }
        }
        if (!builder.isEmpty()) text.add(builder.toString());
        player.openBook(BasicUtil.getBook(1, "个人属性", player.getName(), text));
    }
}
