package cc.ranmc.online.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cc.ranmc.online.util.BasicUtil.color;

public class TearUtil {

    private final static ItemStack tearItem = BasicUtil.getItem(Material.BEETROOT_SOUP, 1,
            "&b龙泪", "&e用于增加可强化次数", "&e拥有古老且神秘力量", "&e垂涎欲滴,或许小酌一口?");
    public final static Map<String,Long> HIT_DRAGON_MAP = new HashMap<>();

    public static ItemStack getTearItem() {
        return tearItem.clone();
    }

    public static boolean isTear(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return item.getType() == Material.BEETROOT_SOUP && meta.hasLore() &&
                color("&e用于增加可强化次数").equals(Objects.requireNonNull(meta.getLore()).getFirst());
    }

    public static boolean cost(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < 45; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && isTear(item)) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                return true;
            }
        }
        return false;
    }
}
