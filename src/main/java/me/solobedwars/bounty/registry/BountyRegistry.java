package me.solobedwars.bounty.registry;

import me.solobedwars.bounty.api.Bounty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface BountyRegistry {

    @NotNull Bounty register(@NotNull UUID uuid, @NotNull Bounty bounty);

    @NotNull Bounty register(@NotNull UUID uuid);

    void unRegister(@NotNull UUID uuid);

    @NotNull Optional<Bounty> get(@NotNull UUID uuid);

    @NotNull Map<UUID, Bounty> getBounties();

    void setOrderedMap(Map<UUID, Bounty> map);

    Map<UUID, Bounty> getOrderedMap();

    void toggleIsSorted();

    boolean isSorted();

    void setSorted(boolean state);

    @NotNull
    default Map<UUID, Bounty> sortedByValues() {

        System.out.println("Sorted: " + isSorted());

        if (!isSorted()) {
            List<Map.Entry<UUID, Bounty>> list = new ArrayList<>(getBounties().entrySet());
            list.sort(Map.Entry.comparingByValue());

            Map<UUID, Bounty> result = new LinkedHashMap<>();
            for (Map.Entry<UUID, Bounty> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }

            setOrderedMap(result);
            setSorted(true);

            return result;
        } else return getOrderedMap();

    }

}
