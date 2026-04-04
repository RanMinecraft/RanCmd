package cc.ranmc.api.util;

public class FoliaUtil {

    private static Boolean folia = null;

    /**
     * 是 Folia 端
     *
     * @return boolean
     */
    public static boolean isFolia() {
        if (folia == null) {
            try {
                Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
                folia = true;
            } catch (ClassNotFoundException ignored) {
                folia = false;
            }
        }
        return folia;
    }
}
