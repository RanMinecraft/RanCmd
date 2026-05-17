package cc.ranmc.online.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicUtil {

    /**
     * 文本颜色
     */
    public static String color(String text) {
        return text.replace("&","§");
    }

    public static String rgbString(@NotNull String text) {
        return translateColorCodes(color(text));
    }

    private static String translateColorCodes(String str) {
        Matcher matcher = Pattern.compile("&#([0-9a-fA-F]){6}|&#([0-9a-fA-F]){3}|§#([0-9a-fA-F]){6}|§#([0-9a-fA-F]){3}").matcher(str);

        StringBuilder sb;
        String hex;
        for(sb = new StringBuilder(); matcher.find(); matcher.appendReplacement(sb, net.md_5.bungee.api.ChatColor.of(hex.substring(1)).toString())) {
            hex = matcher.group();
            if (hex.length() == 5) {
                hex = hex.substring(0, 2) + doubleCharacters(hex.substring(2));
            }
        }

        matcher.appendTail(sb);
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('§', sb.toString());
    }

    private static String doubleCharacters(String str) {
        StringBuilder sb = new StringBuilder();
        char[] var2 = str.toCharArray();
        for (char c : var2) {
            sb.append(c);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String clearColor(@NotNull String text) {
        text = ChatColor.stripColor(color(text));
        if (text.contains("#")) {
            text = Pattern.compile("(?i)#[A-Fa-f0-9]{6}")
                    .matcher(text)
                    .replaceAll("")
                    .replace("§", "");
        }
        return text;
    }

    /**
     * 后台信息
     */
    public static void print(String msg) {
        Bukkit.getConsoleSender().sendMessage(color(msg));
    }

    /**
     * 公屏信息
     */
    public static LocalDateTime getDate(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getDate(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * 获取物品
     */
    public static ItemStack getItem(Material material, int count) {
        return new ItemStack(material,count);
    }

    public static ItemStack getItem(Material material, int count, String name) {
        ItemStack item = new ItemStack(material,count);
        ItemMeta meta = item.getItemMeta();
        if (name != null) Objects.requireNonNull(meta).setDisplayName(color(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItem(Material material, int count, String name, String... lore) {
        return getItem(material, count, name, Arrays.asList(lore));
    }

    public static ItemStack getItem(Material material, int count, String name, List<String> lore) {
        ItemStack item = new ItemStack(material,count);
        ItemMeta meta = item.getItemMeta();
        if (name != null) Objects.requireNonNull(meta).setDisplayName(color(name));
        List<String> newLore = new ArrayList<>();
        lore.forEach(line -> newLore.add(color(line)));
        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getBook(int count, String name, String author, List<String> page) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK, count);
        BookMeta bookmeta = (BookMeta) item.getItemMeta();
        bookmeta.setAuthor(author);
        bookmeta.setTitle(name);
        page.replaceAll(s -> color(s).replace("\\n", "\n"));
        String[] textArray = new String[page.size()];
        page.toArray(textArray);
        bookmeta.addPage(textArray);
        bookmeta.setGeneration(BookMeta.Generation.COPY_OF_ORIGINAL);
        item.setItemMeta(bookmeta);
        return item;
    }

    /**
     * 玩家背包是否已满
     * @param player 玩家
     * @return 是否已满
     */
    public static boolean isInventoryFull(Player player) {
        return isInventoryFull(player.getInventory());
    }

    public static boolean isInventoryFull(Inventory inventory) {
        for (int i = 0; i < 36; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                return false;
            }
        }
        return true;
    }
}
