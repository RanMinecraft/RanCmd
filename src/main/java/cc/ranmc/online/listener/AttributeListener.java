package cc.ranmc.online.listener;

import cc.ranmc.online.Main;
import cc.ranmc.online.util.AttributeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static cc.ranmc.online.util.BasicUtil.color;

public class AttributeListener implements Listener {

    private static final double defense = 1.5;
    private static final int tickTime = 20 * 4;
    private static final Main plugin = Main.getInstance();
    private static final Random random = new Random();

    @EventHandler
    public static void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        boolean isCancel = damager instanceof Egg || damager instanceof Snowball;

        // 玩家头颅
        if (damager instanceof Creeper creeper &&
                damagee instanceof Player player &&
                plugin.getConfig().getBoolean("playerHead")) {
            ItemStack item = player.getInventory().getHelmet();
            if (item != null && item.getType() == Material.PLAYER_HEAD && creeper.isPowered()) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                if (!Objects.requireNonNull(meta).hasOwner()) {
                    meta.setOwningPlayer(player);
                    item.setItemMeta(meta);
                }
            }
        }

        Player pdamager, pdamagee = null;
        Map<String,Integer> damgerAttributeMap, targetAttributeMap = null;
        double damage = event.getDamage();
        boolean combat = true;

        // 玩家间接伤害
        if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player target) damager = target;
            combat = false;
        }
        if (damagee instanceof Player target) {
            pdamagee = target;
            targetAttributeMap = AttributeUtil.getMetaLoreAP(pdamagee);
        }

        // 触发斩杀对象
        LivingEntity slainTarget = null;
        if (damager instanceof Player target) {
            pdamager = target;

            // 雪球鸡蛋伤害不处理
            if (isCancel) return;

            damgerAttributeMap = AttributeUtil.getMetaLoreAP(pdamager);
            damage += damgerAttributeMap.getOrDefault("物理伤害", 0) +
                    damgerAttributeMap.getOrDefault("真实伤害", 0);
            if (combat) {
                damage += damgerAttributeMap.getOrDefault("近战伤害", 0);
            } else {
                damage += damgerAttributeMap.getOrDefault("远程伤害", 0);
            }

            // 是否未触发免疫
            boolean unluck = true;
            // 玩家盔甲韧性
            double toughness = 1;
            // 吸血防御
            int bloodArmor = 0;
            int critArmor = 0;

            // 玩家攻击玩家
            if (pdamagee != null) {

                // 禁止自己伤害自己
                if (pdamager == pdamagee) return;

                //toughness -= (Objects.requireNonNull(pdamagee.getAttribute(org.bukkit.attribute.Attribute.ARMOR)).getValue() / 40);
                bloodArmor = targetAttributeMap.getOrDefault("吸血防御", 0);
                critArmor =  targetAttributeMap.getOrDefault("暴击防御", 0);

                if (targetAttributeMap.getOrDefault("免疫几率", 0) >= random.nextInt(100) + 1) {
                    pdamagee.sendTitle(" ","你免疫了对方的属性效果",1,10,1);
                    pdamager.sendTitle(" ","对方免疫了你的属性效果",1,10,1);
                    unluck = false;
                } else {
                    if (damgerAttributeMap.getOrDefault("冰冻几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了冰冻", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了冰冻", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, tickTime, 5, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.SNOWFLAKE, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                    if (damgerAttributeMap.getOrDefault("流血几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了中毒", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了中毒", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, tickTime, 2, true));
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.POISON, tickTime, 2, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.GLOW_SQUID_INK, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                    if (damgerAttributeMap.getOrDefault("致盲几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了致盲", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了致盲", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, tickTime, 1, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.LARGE_SMOKE, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                    if (damgerAttributeMap.getOrDefault("眩晕几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了眩晕", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了眩晕", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, tickTime, 1, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.SQUID_INK, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                    if (damgerAttributeMap.getOrDefault("虚弱几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了虚弱", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了虚弱", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, tickTime, 0, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.BUBBLE_POP, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                    if (damgerAttributeMap.getOrDefault("混乱几率", 0) >= random.nextInt(100) + 1) {
                        int holdSlot = pdamagee.getInventory().getHeldItemSlot();
                        int newSlot = random.nextInt(9);
                        while (holdSlot == newSlot) newSlot = random.nextInt(9);
                        PlayerItemHeldEvent playerItemHeldEvent = new PlayerItemHeldEvent(pdamagee, holdSlot, newSlot);
                        Bukkit.getPluginManager().callEvent(playerItemHeldEvent);
                        if (!playerItemHeldEvent.isCancelled()) {
                            pdamagee.getInventory().setHeldItemSlot(newSlot);
                            Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.SOUL, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                            pdamagee.sendTitle(" ", "你被对方发动了混乱", 1, 10, 1);
                            pdamager.sendTitle(" ", "你发动了混乱", 1, 10, 1);
                        } else {
                            pdamagee.sendTitle(" ", "对方发动混乱失败了", 1, 10, 1);
                            pdamager.sendTitle(" ", "你发动混乱失败", 1, 10, 1);
                        }
                    }
                    if (damgerAttributeMap.getOrDefault("漂浮几率", 0) >= random.nextInt(100) + 1) {
                        pdamagee.sendTitle(" ", "你被对方发动了漂浮", 1, 10, 1);
                        pdamager.sendTitle(" ", "你发动了漂浮", 1, 10, 1);
                        pdamagee.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, tickTime, 1, true));
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.GLOW, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                    }
                }
            }

            // 玩家攻击实体
            if (pdamager.getAttackCooldown() == 1) {
                // 玩家横扫攻击
                if (damgerAttributeMap.getOrDefault("雷击几率", 0) >= random.nextInt(100) + 1) {
                    damagee.getWorld().strikeLightningEffect(damagee.getLocation());
                    damagee.setFireTicks(tickTime);
                    damage += 10 + damgerAttributeMap.getOrDefault("雷击伤害", 0);
                    if (pdamagee != null) {
                        pdamagee.sendTitle(" ", color("&b你被对方发动了雷击"),1,10,1);
                    }
                    pdamager.sendTitle(" ", color("&b你发动了雷击"),1,10,1);
                }

                if (damgerAttributeMap.getOrDefault("击飞几率", 0) >= random.nextInt(100) + 1) {
                    double x = 0;
                    double z;
                    int yaw = (int) pdamager.getLocation().getYaw();
                    int abs = Math.abs(yaw);
                    if (abs >= 90) {
                        if (abs >= 113) z = -1;
                        else {
                            z = 0;
                        }
                    } else {
                        if (abs <= 73) z = 1;
                        else {
                            z = 0;
                        }
                    }
                    if (yaw >= 23 && yaw <= 153)  x = -1;
                    if (yaw >= -153 && yaw <= -23)  x = 1;

                    WindCharge windCharge = damagee.getLocation().getWorld().spawn(damagee.getLocation(), WindCharge.class);
                    windCharge.explode();
                    double finalX = x;
                    plugin.getFoliaLib().getScheduler().runAtEntityLater(damagee, () ->
                            damagee.setVelocity(new Vector(finalX, 1.2, z)), 1);
                    if (pdamagee != null) {
                        pdamagee.setVelocity(new Vector(x, 3, z));
                        pdamagee.sendTitle(" ", "你被对方发动了击飞",1,10,1);
                    }
                    pdamager.sendTitle(" ", "你发动了击飞",1,10,1);
                }

                if (damgerAttributeMap.getOrDefault("斩杀几率", 0) >= random.nextInt(100) + 1) {
                    if (pdamagee != null) {
                        if (pdamagee.getHealth() < 10) {
                            pdamagee.sendTitle(" ",color("&c你被对方发动了斩杀"),1,10,1);
                            pdamagee.playSound(damagee.getLocation(), Sound.ENTITY_ALLAY_HURT, 1 , 0);
                            slainTarget = pdamagee;
                        }
                    } else if (!damagee.isDead() &&
                            damagee instanceof LivingEntity livingEntity &&
                            livingEntity.getHealth() < 20) {
                        slainTarget = livingEntity;
                    }
                    if (slainTarget != null) {
                        Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.GLOW, damagee.getLocation(), 20, 0.5, 1, 0.5, 0.02);
                        pdamager.sendTitle(" ", color("&c你发动了斩杀"),1,10,1);
                        pdamager.playSound(damagee.getLocation(), Sound.ENTITY_ALLAY_HURT, 1 , 0);
                    }
                }
            }

            if (unluck) {
                if (damgerAttributeMap.getOrDefault("吸血几率", 0) >= random.nextInt(100) + 1 && !pdamager.isDead()) {
                    if (pdamagee != null) pdamagee.sendTitle(" ", "你被对方发动了吸血", 1, 10, 1);
                    pdamager.sendTitle(" ", "你发动了吸血", 1, 10, 1);
                    int bloodDamage = damgerAttributeMap.getOrDefault("吸血伤害", 0) - bloodArmor;
                    if (bloodDamage < 2) bloodDamage = 2;
                    if (bloodDamage > 19) bloodDamage = 19;
                    double maxHealth = Objects.requireNonNull(pdamager.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH)).getValue();
                    pdamager.setHealth(Math.min((pdamager.getHealth() + bloodDamage), maxHealth));
                    Objects.requireNonNull(damager.getLocation().getWorld()).spawnParticle(Particle.HEART, damager.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                }

                if (damgerAttributeMap.getOrDefault("暴击几率", 0) >= random.nextInt(100) + 1) {
                    if (pdamagee != null) pdamagee.sendTitle(" ", "你被对方发动了暴击", 1, 10, 1);
                    pdamager.sendTitle(" ", "你发动了暴击", 1, 10, 1);
                    int critDamage = damgerAttributeMap.getOrDefault("暴击伤害", 0) - critArmor;
                    if (critDamage < 0) critDamage = 1;
                    damage += critDamage * 2 + 2;
                    assert pdamagee != null;
                    Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.EXPLOSION, damagee.getLocation(), 2, 0.3, 1, 0.3, 0.01);
                }
            }
            damage = damage * (combat ? pdamager.getAttackCooldown() : 1) * toughness;
        } else {
            pdamager = null;
        }

        // 玩家受到伤害
        if (pdamagee != null) {
            if (targetAttributeMap.getOrDefault("闪避几率", 0) >= random.nextInt(100) + 1) {
                if (slainTarget == null) {
                    pdamagee.sendActionBar(color("&c你闪避了本次攻击"));
                    if (pdamager != null) pdamager.sendActionBar(color("&c对方闪避了本次攻击"));
                } else {
                    pdamagee.sendActionBar(color("&c你闪避了本次斩杀"));
                    pdamager.sendActionBar(color("&c对方闪避了本次斩杀"));
                }
                Objects.requireNonNull(damagee.getLocation().getWorld()).spawnParticle(Particle.COMPOSTER, damagee.getLocation(),10,0.2,1,0.2,0.01);
                event.setCancelled(true);
                double x = (Math.random() / 2) + 0.5;
                if (random.nextBoolean()) x = -x;
                double z = (Math.random() / 2) + 0.5;
                if (random.nextBoolean()) z = -z;
                pdamagee.setVelocity(new Vector(x, 0 , z));
                return;
            }
            if (pdamager == null) {
                // 来自非玩家伤害
                damage -= ((double) (targetAttributeMap.getOrDefault("物理防御", 0) +
                        targetAttributeMap.getOrDefault(combat ? "近战防御" : "远程防御", 0))) / defense;
            } else {
                // 来自玩家伤害
                damgerAttributeMap = AttributeUtil.getMetaLoreAP(pdamager);
                double playerDefense = targetAttributeMap.getOrDefault("物理防御", 0)
                        + targetAttributeMap.getOrDefault(combat ? "近战防御" : "远程防御", 0)
                        - damgerAttributeMap.getOrDefault("真实伤害", 0);
                if (playerDefense > 0) damage -= playerDefense / defense;
                if (targetAttributeMap.getOrDefault("反弹几率", 0) >= random.nextInt(100) + 1) {
                    if (slainTarget != null) {
                        slainTarget = pdamager;
                        plugin.getFoliaLib().getScheduler().runAtEntity(pdamager, _ ->
                                pdamager.damage(1));
                        Objects.requireNonNull(damagee.getLocation().getWorld())
                                .spawnParticle(Particle.GLOW, damagee.getLocation(), 20, 0.5, 1, 0.5, 0.02);
                        pdamagee.sendTitle(" ", "&c你反弹了对方的斩杀", 1, 10, 1);
                        pdamager.sendTitle(" ", color("&c对方反弹了你的斩杀"),1,10,1);
                        pdamager.playSound(damagee.getLocation(), Sound.ENTITY_ALLAY_HURT, 1 , 0);
                    } else {
                        pdamagee.sendTitle(" ", "你反弹了本次攻击", 1, 10, 1);
                        pdamager.sendTitle(" ", "对方反弹了本次攻击", 1, 10, 1);
                        pdamager.damage(damage * 0.5);
                    }
                    damage = 0;
                    Objects.requireNonNull(damagee.getLocation().getWorld())
                            .spawnParticle(Particle.ELECTRIC_SPARK, damagee.getLocation(), 5, 0.3, 1, 0.3, 0.01);
                }
            }
        }
        if (slainTarget != null && !slainTarget.isDead()) {
            if (slainTarget instanceof Player slainPlayer) {
                slainPlayer.setHealth(0.1);
            } else {
                slainTarget.setHealth(0.1);
            }
        }

        if (0 > damage) damage = 0;
        event.setDamage(damage);

        double finalDamage = event.getFinalDamage();
        String damageText = finalDamage >= 1 ? String.format("%.0f", finalDamage) : String.format("%.1f", finalDamage);
        if (pdamager != null) pdamager.sendActionBar("你造成了 "+ damageText + " 伤害");
        if (pdamagee != null) pdamagee.sendActionBar("你受到了 "+ damageText + " 伤害");
    }
}