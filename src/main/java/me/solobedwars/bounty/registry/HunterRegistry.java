package me.solobedwars.bounty.registry;

import me.solobedwars.bounty.api.Hunter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface HunterRegistry {

    @NotNull Hunter register(@NotNull UUID uuid, @NotNull Hunter hunter);

    @NotNull Hunter register(@NotNull UUID uuid);

    void unRegister(@NotNull UUID uuid);

    @NotNull Optional<Hunter> get(@NotNull UUID uuid);

    @NotNull Map<UUID, Hunter> getHunters();

    @NotNull
    default Map<UUID, Hunter> sortedByValues() {

        List<Map.Entry<UUID, Hunter>> list = new ArrayList<>(getHunters().entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<UUID, Hunter> result = new LinkedHashMap<>();
        for (Map.Entry<UUID, Hunter> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;

    }

}
