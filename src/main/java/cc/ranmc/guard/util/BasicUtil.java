package cc.ranmc.guard.util;

import org.bukkit.Bukkit;

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
    public static void say(String msg) {
        Bukkit.broadcastMessage(color(msg));
    }

    /**
     * 是 Folia 端
     *
     * @return boolean
     */
    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

}
