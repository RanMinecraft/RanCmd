package cc.ranmc.online.listener;

import cc.ranmc.online.util.TearUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Creeper creeper &&
                event.getEntity() instanceof Player player) {
            ItemStack item = player.getInventory().getHelmet();
            if (item != null && item.getType() == Material.PLAYER_HEAD && creeper.isPowered()) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                if (!Objects.requireNonNull(meta).hasOwner()) {
                    meta.setOwningPlayer(player);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Location location = event.getEntity().getLocation();
            Objects.requireNonNull(location.getWorld()).dropItem(location, TearUtil.getTearItem()).setGlowing(true);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Item item) {
            event.setCancelled(true);
            item.remove();
        }
        if (entity instanceof Snowball snowball) {
            event.setCancelled(true);
            snowball.remove();
        }
        if (entity.getType().toString().endsWith("MINECART")) {
            event.setCancelled(true);
            entity.remove();
        }
    }
}
