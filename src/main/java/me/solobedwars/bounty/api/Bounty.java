package me.solobedwars.bounty.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.lucko.helper.gson.GsonSerializable;
import me.lucko.helper.terminable.Terminable;
import me.solobedwars.bounty.factory.SkullFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Bounty extends GsonSerializable, Terminable, Comparable<Bounty> {

    static @NotNull SimpleBounty deserialize(JsonElement element) {
        final JsonObject object = element.getAsJsonObject();

        final UUID uuid = UUID.fromString(object.get("target").getAsString());

        final Set<Map.Entry<String, JsonElement>> contributions = object.getAsJsonObject("contributors").entrySet();

        SimpleBounty bounty = new SimpleBounty(uuid);

        contributions.forEach(entry -> bounty.addContribution(UUID.fromString(entry.getKey()), entry.getValue().getAsDouble()));

        return bounty;
    }

    @NotNull UUID getTarget();

    @NotNull
    default ItemStack getSkull(Player player) {
        return SkullFactory.make(player, this);
    }

    @NotNull
    default ItemStack getSkullListing() {
        return SkullFactory.makeListing(this);
    }

    @NotNull String getTargetName();

    void setTargetName(@NotNull String name);

    long getLastDeath();

    void setLastDeath();

    @NotNull Map<UUID, Double> getContributors();

    void addContribution(@NotNull UUID uuid, double value);

    double getReward();

    default Double getContribution(@NotNull UUID uuid) {
        return getContributors().get(uuid);
    }

    default int compareTo(Bounty a) {
        return Double.compare(a.getReward(), this.getReward());
    }

}
