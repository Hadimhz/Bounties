package me.solobedwars.bounty.registry;

import me.solobedwars.bounty.api.Hunter;
import me.solobedwars.bounty.api.SimpleHunter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHunterRegistry implements HunterRegistry {

    private final Map<UUID, Hunter> hunters = new ConcurrentHashMap<>();


    @Override
    public @NotNull Hunter register(@NotNull UUID uuid, @NotNull Hunter hunter) {

        hunters.put(uuid, hunter);


        return hunter;
    }

    @Override
    public @NotNull Hunter register(@NotNull UUID uuid) {

        final Hunter hunter = new SimpleHunter(uuid);

        hunters.put(uuid, hunter);
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
}
