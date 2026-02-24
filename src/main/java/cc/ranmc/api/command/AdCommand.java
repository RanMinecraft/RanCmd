package cc.ranmc.api.command;

import cc.ranmc.api.Main;
import cc.ranmc.api.util.BasicUtil;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cc.ranmc.api.util.BasicUtil.color;

public class AdCommand implements CommandExecutor {
    
    private final static Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NonNull Command cmd,
                             @NotNull String label,
                             String @NonNull [] args) {


        if (!sender.hasPermission("roa.user")) {
            sender.sendMessage(color("&a没有权限"));
            return true;
        }

        // 以下指令不能在控制台输入
        if (!(sender instanceof Player player)) {
            BasicUtil.print(color("&c该指令不能在控制台输入"));
            return true;
        }

        if (args.length == 0) {
            // 打开宣传栏
            List<String> adList = new ArrayList<>();
            LocalDate dt = LocalDate.now();
            List<String> outdatedList = plugin.getConfig().getStringList("ad-list");
            for (String string : outdatedList) {
                String[] endtime = string.split(" ")[3].split("-");
                if (dt.getYear() < Integer.parseInt(endtime[0])) {
                    adList.add(string);
                } else if (dt.getYear() == Integer.parseInt(endtime[0]) && dt.getMonthValue() < Integer.parseInt(endtime[1])) {
                    adList.add(string);
                } else if (dt.getYear() == Integer.parseInt(endtime[0]) && dt.getMonthValue() == Integer.parseInt(endtime[1]) && dt.getDayOfMonth() < Integer.parseInt(endtime[2])) {
                    adList.add(string);
                }
            }

            if (adList.size()<outdatedList.size()) {
                plugin.getConfig().set("ad-list", adList);
                plugin.saveConfig();
            }

            //打开宣传栏
            int guisize = 9;
            if(adList.isEmpty()) {
                adList = new ArrayList<>();
            } else if(adList.size() >= 36) {
                guisize = 45;
            } else if(adList.size() >= 27) {
                guisize = 36;
            } else if(adList.size() >= 18) {
                guisize = 27;
            } else if(adList.size() >= 9) {
                guisize = 18;
            }
            Inventory inv = Bukkit.createInventory(null, guisize, color("&b&l领地宣传栏"));

            ItemStack item10 = new ItemStack(Material.COMPASS);
            ItemMeta meta10 = item10.getItemMeta();
            meta10.setDisplayName(color("&b宣传栏"));
            ArrayList<String> lore1 = new ArrayList<>();
            lore1.add(color("&e/ad 领地 介绍"));
            lore1.add(color("&e花费" + plugin.getConfig().getInt("ad-price") + "金币/周"));
            lore1.add(color("&9发布你的领地和介绍"));
            lore1.add(color("&9让更多人了解和向往"));
            lore1.add(color("&9请勿发布辱骂等信息"));
            lore1.add(color("&9一经发现将封禁处理"));
            meta10.setLore(lore1);
            item10.setItemMeta(meta10);

            inv.setItem(0, item10);

            for (int i = 0; i < adList.size(); i++) {
                String[] resinfo = adList.get(i).split(" ");
                ItemStack item = new ItemStack(Material.OAK_SIGN);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(color("&e领地：&c"+resinfo[0]));
                ArrayList<String> lore2 = new ArrayList<>();
                lore2.add(color("&e发布者：&c"+resinfo[1]));
                lore2.add(color("&e到期日：&c"+resinfo[3]));
                lore2.add(color("&e介绍描述："));
                for (String line : resinfo[2].split("\\\\n")) {
                    lore2.add(color(line));
                }
                lore2.add(color("&b点击传送至领地"));
                meta.setLore(lore2);
                item.setItemMeta(meta);

                inv.setItem(1 + i, item);
            }

            player.openInventory(inv);
        } else {
            // 创建宣传栏
            Economy econ = Main.getEcon();
            int price = plugin.getConfig().getInt("ad-price");
            if (econ.getBalance(player) < price) {
                sender.sendMessage(color("&c你的金券不足"));
                return true;
            }
            List<String> adlist = plugin.getConfig().getStringList("ad-list");
            if (adlist.size() >= 44) {
                sender.sendMessage(color("&c当前宣传栏已满,明天再来吧"));
                return true;
            }
            LocalDateTime dt = LocalDateTime.now();
            LocalDateTime endtime = dt.plusDays(7);

            ClaimedResidence residence = ResidenceApi.getResidenceManager().getByName(args[0]);
            if (residence == null) {
                sender.sendMessage(color("&c该领地不存在"));
                return true;
            }
            if (args[1].length() > plugin.getConfig().getInt("ad-length", 16)) {
                sender.sendMessage(color("&c介绍字数过长"));
                return true;
            }
            adlist.add(args[0]+" " + player.getName() + " " + args[1] + " "+
                    endtime.getYear()+"-"+endtime.getMonthValue()+"-"+endtime.getDayOfMonth());
            plugin.getConfig().set("ad-list", adlist);
            plugin.saveConfig();
            econ.withdrawPlayer(player, price);
            sender.sendMessage(color("&a创建成功,快打开宣传栏查看吧"));
        }
        return true;
    }
}
