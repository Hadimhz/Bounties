package me.solobedwars.bounty.Listener;

import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.factory.SkullFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements TerminableModule {

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {

        // Make sure the name is always up-to-date
        Events.subscribe(PlayerJoinEvent.class).handler(event -> {

            Player p = event.getPlayer();

            BountiesApi.getBountyFrom(p.getUniqueId()).ifPresent(bounty -> bounty.setTargetName(p.getName()));

            BountiesApi.getHunterFrom(p.getUniqueId()).ifPresent(hunter -> hunter.setPlayerName(p.getName()));

        }).bindWith(consumer);

        Events.subscribe(PlayerDeathEvent.class).handler(event -> {

            Player player = event.getEntity();

            BountiesApi.getBountyFrom(player.getUniqueId()).ifPresent(bounty -> {

                bounty.setLastDeath();

                player.getWorld().dropItem(player.getLocation(), SkullFactory.make(player, bounty));

            });

        });

    }


}
