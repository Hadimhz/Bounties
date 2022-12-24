package me.solobedwars.bounty.api;

import com.google.gson.JsonElement;
import me.lucko.helper.gson.JsonBuilder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SimpleHunter implements Hunter {


    private final UUID player;
    private String playerName;
    private double moneyClaimed;
    private int bountiesClaimed;

    public SimpleHunter(UUID player, double moneyClaimed, int bountiesClaimed) {

        this.player = player;
        this.moneyClaimed = moneyClaimed;
        this.bountiesClaimed = bountiesClaimed;

        playerName = Bukkit.getOfflinePlayer(player).getName();

    }

    public SimpleHunter(UUID player) {
        this.player = player;
        this.moneyClaimed = 0;
        this.bountiesClaimed = 0;

        playerName = Bukkit.getOfflinePlayer(player).getName();

    }

    @Override
    public void claimBounty(Bounty bounty) {
        bountiesClaimed++;
        moneyClaimed += bounty.getReward();
    }

    @Override
    public @NotNull UUID getPlayer() {
        return this.player;
    }

    @Override
    public @NotNull String getPlayerName() {
        return playerName;
    }

    @Override
    public void setPlayerName(@NotNull String name) {
        playerName = name;
    }

    @Override
    public int getClaimed() {
        return this.bountiesClaimed;
    }

    @Override
    public double getTotalRewards() {
        return this.moneyClaimed;
    }

    @NotNull
    @Override
    public JsonElement serialize() {
        return JsonBuilder.object()
                .add("uuid", player.toString())
                .add("playerName", playerName)
                .add("bountiesClaimed", bountiesClaimed)
                .add("moneyClaimed", moneyClaimed)
                .build();
    }
}
