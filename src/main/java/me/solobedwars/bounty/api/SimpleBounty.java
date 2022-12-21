package me.solobedwars.bounty.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.lucko.helper.gson.JsonBuilder;
import me.lucko.helper.terminable.composite.AbstractCompositeTerminable;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleBounty extends AbstractCompositeTerminable implements Bounty {

    private UUID target;
    private Map<UUID, Double> contributors;
    private String targetName;
    private double reward;
    private long lastDeath;

    public SimpleBounty(UUID target, double reward, Map<UUID, Double> contributors) {
        this.target = target;
        this.contributors = contributors;
        this.reward = reward;

        targetName = Bukkit.getOfflinePlayer(target).getName();
    }

    public SimpleBounty(UUID target, UUID player, double reward) {
        this.target = target;
        this.contributors = new HashMap<>();
        addContribution(player, reward);

        targetName = Bukkit.getOfflinePlayer(target).getName();

    }

    public SimpleBounty(UUID target) {
        this.target = target;
        this.contributors = new HashMap<>();

        targetName = Bukkit.getOfflinePlayer(target).getName();
    }

    public @NotNull UUID getTarget() {
        return target;
    }

    @Override
    public @NotNull String getTargetName() {
        return targetName;
    }

    @Override
    public void setTargetName(@NotNull String name) {
        targetName = name;
    }

    @Override
    public long getLastDeath() {
        return lastDeath;
    }

    @Override
    public void setLastDeath() {
        lastDeath = System.currentTimeMillis();
    }

    public @NotNull Map<UUID, Double> getContributors() {
        return contributors;
    }

    public void addContribution(@NotNull UUID player, double value) {
        reward += value;
        BountiesApi.getBountyRegistry().toggleIsSorted();
        contributors.put(player, contributors.getOrDefault(player, 0d) + value);
    }

    public double getReward() {
        return reward;
    }

    @Override
    public void close() {
        BountiesApi.getBountyRegistry().unRegister(target);
        this.target = null;
        this.contributors = null;
        this.targetName = null;
    }

    @NotNull
    @Override
    public JsonElement serialize() {


        JsonObject con = JsonBuilder.object().build();

        contributors.forEach(((uuid, aDouble) -> {
            con.addProperty(uuid.toString(), aDouble);
        }));


        return JsonBuilder.object()
                .add("target", target.toString())
                .add("targetName", targetName)
                .add("contributors", con)
                .build();
    }
}
