package me.solobedwars.bounty.registry;

import me.solobedwars.bounty.api.Hunter;
import me.solobedwars.bounty.api.SimpleHunter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHunterRegistry implements HunterRegistry {

    private final Map<UUID, Hunter> hunters = new ConcurrentHashMap<>();
    private final boolean isSorted = false;
    private Map<UUID, Hunter> orderedHunters = Collections.synchronizedMap(new LinkedHashMap<>());

    @Override
    public @NotNull Hunter register(@NotNull UUID uuid, @NotNull Hunter hunter) {

        hunters.put(uuid, hunter);
        orderedHunters.put(uuid, hunter);
        toggleIsSorted();
        return hunter;
    }

    @Override
    public @NotNull Hunter register(@NotNull UUID uuid) {

        final Hunter hunter = new SimpleHunter(uuid);
        orderedHunters.put(uuid, hunter);
        hunters.put(uuid, hunter);
        toggleIsSorted();
        return hunter;
    }

    @Override
    public void unRegister(@NotNull UUID uuid) {
        hunters.remove(uuid);
    }

    @Override
    public @NotNull Optional<Hunter> get(@NotNull UUID uuid) {
        return this.hunters.containsKey(uuid) ? Optional.of(this.hunters.get(uuid)) : Optional.empty();
    }

    @Override
    public @NotNull Map<UUID, Hunter> getHunters() {
        return hunters;
    }

    @Override
    public Map<UUID, Hunter> getOrderedMap() {
        return orderedHunters;
    }

    @Override
    public void setOrderedMap(Map<UUID, Hunter> map) {
        orderedHunters = map;
    }

    @Override
    public void toggleIsSorted() {
        setSorted(false);
    }

    @Override
    public boolean isSorted() {
        return false;
    }

    @Override
    public void setSorted(boolean state) {

    }
}
