package me.solobedwars.bounty.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.lucko.helper.gson.GsonSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Hunter extends GsonSerializable, Comparable<Hunter> {

    static @NotNull SimpleHunter deserialize(JsonElement element) {
        final JsonObject object = element.getAsJsonObject();

        final UUID uuid = UUID.fromString(object.get("player").getAsString());
        final double money = object.get("moneyClaimed").getAsDouble();
        final int bounties = object.get("bountiesClaimed").getAsInt();


        return new SimpleHunter(uuid, money, bounties);
    }


    @NotNull UUID getPlayer();

    @NotNull String getPlayerName();

    void setPlayerName(@NotNull String name);

    int getClaimed();

    double getTotalRewards();

    void claimBounty(Bounty bounty);

    default int compareTo(Hunter a) {
        return Double.compare(a.getTotalRewards(), this.getTotalRewards());
    }

}
