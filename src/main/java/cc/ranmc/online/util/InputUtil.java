package cc.ranmc.online.util;

import cc.ranmc.online.bean.InputCallback;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static cc.ranmc.online.util.BasicUtil.color;

public class InputUtil {

    @Getter
    private static final Map<String, InputCallback> inputMap = new HashMap<>();

    public static void open(Player player, String text, InputCallback callback) {
        TranslatableComponent textComponent = new TranslatableComponent(color("&a请打开输入框输入&e" + text));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("请在聊天框输入" + text).create()));
        TextComponent cancelBtn = new TextComponent(color(" &c[取消]"));
        cancelBtn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("点击取消输入状态").create()));
        cancelBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cancelinput"));
        textComponent.addExtra(cancelBtn);
        player.spigot().sendMessage(textComponent);
        inputMap.put(player.getName(), callback);
        player.closeInventory();
    }
}
