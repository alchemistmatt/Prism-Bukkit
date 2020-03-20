package me.botsko.prism.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class DeathUtils {

    /**
     * Returns the name of what caused an entity to die.
     *
     * @return
     */
    public static String getCauseNiceName(Entity entity) {

        EntityDamageEvent e = entity.getLastDamageCause();

        if (e == null) {
            return "unknown";
        }

        // Determine the root cause
        DamageCause damageCause = e.getCause();
        Entity killer = null;

        // If was damaged by an entity
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity
                  .getLastDamageCause();
            // Arrow?
            if (entityDamageByEntityEvent.getDamager() instanceof Arrow) {
                Projectile arrow = (Arrow) entityDamageByEntityEvent.getDamager();
                ProjectileSource source = arrow.getShooter();
                if (source instanceof Player) {
                    killer = ((Player) source);
                }
            } else {
                killer = entityDamageByEntityEvent.getDamager();
            }
        }

        if (entity instanceof Player) {

            Player player = (Player) entity;

            // Detect additional suicide. For example, when you potion
            // yourself with instant damage it doesn't show as suicide.
            if (killer instanceof Player) {
                // Themself
                if (killer.getName().equals(player.getName())) {
                    return "suicide";
                }
                // translate bukkit events to nicer names
                if ((damageCause.equals(DamageCause.ENTITY_ATTACK) || damageCause.equals(DamageCause.PROJECTILE))) {
                    return "pvp";
                }
            }
        }

        // Causes of death for either entities or players
        if (damageCause.equals(DamageCause.ENTITY_ATTACK)) {
            return "mob";
        } else if (damageCause.equals(DamageCause.PROJECTILE)) {
            return "skeleton";
        } else if (damageCause.equals(DamageCause.ENTITY_EXPLOSION)) {
            return "creeper";
        } else if (damageCause.equals(DamageCause.CONTACT)) {
            return "cactus";
        } else if (damageCause.equals(DamageCause.BLOCK_EXPLOSION)) {
            return "tnt";
        } else if (damageCause.equals(DamageCause.FIRE) || damageCause.equals(DamageCause.FIRE_TICK)) {
            return "fire";
        } else if (damageCause.equals(DamageCause.MAGIC)) {
            return "potion";
        }
        return damageCause.name().toLowerCase();
    }

    /**
     * Returns the name of the attacker, whether mob or player.
     *
     * @return
     */
    public static String getAttackerName(Entity victim) {

        // Determine base cause
        String cause = getCauseNiceName(victim);

        if (victim instanceof Player) {
            Player killer = ((Player) victim).getKiller();
            if (killer != null) {
                return killer.getName();
            }
        }

        if (cause.equals("mob")) {

            Entity killer = ((EntityDamageByEntityEvent) victim.getLastDamageCause()).getDamager();

            // Playa!
            if (killer instanceof Player) {
                return killer.getName();
            }
            // Which skeleton type?
            // TODO: Other skeleton types
            else if (killer instanceof Skeleton) {
                if (killer instanceof WitherSkeleton) {
                    return "witherskeleton";
                } else {
                    return "skeleton";
                }
            }
            // Shot!
            else if (killer instanceof Arrow) {
                return "skeleton";
            }
            // Aggressive wolves
            else if (killer instanceof Wolf) {
                Tameable wolf = (Wolf) killer;
                if (wolf.isTamed()) {
                    if (wolf.getOwner() instanceof Player || wolf.getOwner() instanceof OfflinePlayer) {
                        return "pvpwolf";
                    } else {
                        return "wolf";
                    }
                } else {
                    return "wolf";
                }
            } else {
                return killer.getType().name().toLowerCase();
            }
        }
        return cause;
    }

    /**
     * Returns the name of the attacker, whether mob or player.
     *
     * @return
     */
    public static String getVictimName(Entity victim) {

        if (victim instanceof Player) {
            return victim.getName();
        } else {

            // Which skeleton type?
            // TODO: Other skeleton types
            if (victim instanceof Skeleton) {
                if (victim instanceof WitherSkeleton) {
                    return "witherskeleton";
                } else {
                    return "skeleton";
                }
            }
            // Shot!
            else if (victim instanceof Arrow) {
                return "skeleton";
            }
            // Aggressive wolves
            else if (victim instanceof Wolf) {
                Tameable wolf = (Wolf) victim;
                if (wolf.isTamed()) {
                    if (wolf.getOwner() instanceof Player || wolf.getOwner() instanceof OfflinePlayer) {
                        return "pvpwolf";
                    } else {
                        return "wolf";
                    }
                } else {
                    return "wolf";
                }
            } else {
                return victim.getType().name().toLowerCase();
            }
        }
    }

    /**
     * Determines the owner of a tamed wolf.
     *
     * @param event
     * @return
     */
    public static String getTameWolfOwner(EntityDeathEvent event) {
        String owner = "";
        Entity killer = ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager();
        if (killer instanceof Wolf) {
            Wolf wolf = (Wolf) killer;
            if (wolf.isTamed()) {
                if (wolf.getOwner() instanceof Player) {
                    owner = ((Player) wolf.getOwner()).getName();
                }
                if (wolf.getOwner() instanceof OfflinePlayer) {
                    owner = wolf.getOwner().getName();
                }
            }
        }
        return owner;
    }

    /**
     * Determines the weapon used to kill an entity.
     *
     * @return
     */
    public static String getWeapon(LivingEntity entity) {
        String death_weapon = "";
        if (entity.getKiller() != null) {
            ItemStack weapon = entity.getKiller().getInventory().getItemInMainHand();
            death_weapon = weapon.getType().toString().toLowerCase();
            death_weapon = death_weapon.replaceAll("_", " ");
            if (death_weapon.equalsIgnoreCase("air")) {
                death_weapon = " hands";
            }
        }
        return death_weapon;
    }
}