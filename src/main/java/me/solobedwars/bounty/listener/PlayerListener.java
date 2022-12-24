package me.solobedwars.bounty.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.factory.SkullFactory;
import me.solobedwars.bounty.registry.Config;
import me.solobedwars.bounty.util.Chat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements TerminableModule {

    private final Config config;

    public PlayerListener(Config config) {
        this.config = config;
    }

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {

        // Make sure the name is always up-to-date
        Events.subscribe(PlayerJoinEvent.class).handler(event -> {

            Player p = event.getPlayer();

            BountiesApi.getBountyFrom(p.getUniqueId()).ifPresent(bounty -> bounty.setTargetName(p.getName()));

            BountiesApi.getHunterFrom(p.getUniqueId()).ifPresent(hunter -> hunter.setPlayerName(p.getName()));

        }).bindWith(consumer);

        Events.subscribe(EntityDamageByEntityEvent.class).handler(event -> {

            if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player || event.getDamager() instanceof Projectile))
                return;


            Player player = (Player) event.getEntity();

            Player killer;

            if (event.getDamager() instanceof Projectile) {
                ProjectileSource damager = ((Projectile) event.getDamager()).getShooter();

                if (!(damager instanceof Player)) return;
                else killer = (Player) damager;

            } else {
                killer = (Player) event.getDamager();
            }


            if (player.getHealth() > event.getDamage()) return;

            if (player.getUniqueId() == killer.getUniqueId()) return;

            long time = System.currentTimeMillis();

            BountiesApi.getBountyFrom(player.getUniqueId()).ifPresent(bounty -> {

                if ((bounty.getLastDeath() + (config.BOUNTY_COOLDOWN * 1000L)) > time) return;

                bounty.setLastDeath(time);
                player.getWorld().dropItem(player.getLocation(), SkullFactory.make(killer, bounty));

            });

        });

        Events.subscribe(BlockPlaceEvent.class).handler(event -> {
            NBTItem nbt = new NBTItem(event.getItemInHand());

            if (nbt.hasKey("bounty")) {

                event.getPlayer().sendMessage(Chat.colorFormat("%s&cYou can't place this head, take it to the warden to claim the bounty!", config.PREFIX));

                event.setCancelled(true);
            }

        });

    }

}
