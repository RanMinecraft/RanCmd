package cc.ranmc.online.listener;

import cc.ranmc.online.util.InputUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            Player player = event.getPlayer();
            if (player.hasPermission("roa.joinfullserver")) {
                event.allow();
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (InputUtil.getInputMap().containsKey(player.getName())) {
            InputUtil.getInputMap().get(player.getName()).onCallback(event.getMessage());
            InputUtil.getInputMap().remove(player.getName());
            event.setCancelled(true);
        }
    }

}
