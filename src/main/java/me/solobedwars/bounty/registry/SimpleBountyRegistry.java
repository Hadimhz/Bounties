package me.solobedwars.bounty.registry;

import me.solobedwars.bounty.api.Bounty;
import me.solobedwars.bounty.api.SimpleBounty;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBountyRegistry implements BountyRegistry {

    private Map<UUID, Bounty> orderedBounties = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<UUID, Bounty> bounties = new ConcurrentHashMap<>();
    private boolean isSorted = false;

    @Override
    public @NotNull Bounty register(@NotNull UUID uuid, @NotNull Bounty bounty) {
        bounties.put(uuid, bounty);

        return bounty;
    }

    @Override
    public @NotNull Bounty register(@NotNull UUID uuid) {
        Bounty bounty = new SimpleBounty(uuid);
        bounties.put(uuid, bounty);
        toggleIsSorted();
        return bounty;
    }

    @Override
    public void unRegister(@NotNull UUID uuid) {
        bounties.remove(uuid);
        orderedBounties.remove(uuid);
    }

    @Override
    public @NotNull Optional<Bounty> get(@NotNull UUID uuid) {
        return this.bounties.containsKey(uuid) ? Optional.of(this.bounties.get(uuid)) : Optional.empty();
    }

    @Override
    public @NotNull Map<UUID, Bounty> getBounties() {
        return bounties;
    }

    @Override
    public Map<UUID, Bounty> getOrderedMap() {
        return orderedBounties;
    }

    @Override
    public void setOrderedMap(Map<UUID, Bounty> map) {
        orderedBounties = map;
    }

    @Override
    public void toggleIsSorted() {
        setSorted(false);
    }

    @Override
    public boolean isSorted() {
        return isSorted;
    }

    @Override
    public void setSorted(boolean state) {
        this.isSorted = state;
    }
}
