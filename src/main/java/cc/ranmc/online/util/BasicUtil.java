package cc.ranmc.online.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BasicUtil {

    /**
     * 文本颜色
     */
    public static String color(String text) {
        return text.replace("&","§");
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

}
