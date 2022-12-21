package me.solobedwars.bounty.api;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Item;
import me.solobedwars.bounty.registry.BountyRegistry;
import me.solobedwars.bounty.registry.HunterRegistry;
import me.solobedwars.bounty.registry.SimpleBountyRegistry;
import me.solobedwars.bounty.registry.SimpleHunterRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BountiesApi {

    private static final BountyRegistry bountyRegistry;
    private static final HunterRegistry hunterRegistry;


    static {
        bountyRegistry = new SimpleBountyRegistry();
        hunterRegistry = new SimpleHunterRegistry();
    }

    public static @NotNull Optional<Hunter> getHunterFrom(@NotNull UUID uuid) {
        return hunterRegistry.get(uuid);
    }

    public static @NotNull Optional<Bounty> getBountyFrom(@NotNull UUID uuid) {
        return bountyRegistry.get(uuid);
    }

    public static @NotNull List<Item> getOrderedBountiesSkulls() {
        return getBountyRegistry().sortedByValues().values().stream().map(bounty -> ItemStackBuilder.of(bounty.getSkullListing()).build(() -> {
                }))
                .collect(Collectors.toList());

    }


    public static @NotNull Hunter getOrCreateHunter(@NotNull UUID uuid) {
        return getHunterFrom(uuid).orElseGet(() -> hunterRegistry.register(uuid));
    }

    public static @NotNull Bounty getOrCreateBounty(@NotNull UUID uuid) {
        return getBountyFrom(uuid).orElseGet(() -> bountyRegistry.register(uuid));
    }

    public static BountyRegistry getBountyRegistry() {
        return bountyRegistry;
    }

    public static HunterRegistry getHunterRegistry() {
        return hunterRegistry;
    }

    public static Map<UUID, Hunter> getHunters() {
        return getHunterRegistry().getHunters();
    }

    public static Map<UUID, Bounty> getBounties() {
        return getBountyRegistry().getBounties();
    }

}
