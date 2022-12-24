package me.solobedwars.bounty.plugin;

import co.aikar.commands.PaperCommandManager;
import com.google.common.reflect.TypeToken;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.serialize.GsonStorageHandler;
import me.solobedwars.bounty.listener.PlayerListener;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.api.Bounty;
import me.solobedwars.bounty.api.Hunter;
import me.solobedwars.bounty.command.BountyCommand;
import me.solobedwars.bounty.hook.VaultHook;
import me.solobedwars.bounty.registry.Config;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class BountyPlugin extends ExtendedJavaPlugin {
    private static BountyPlugin instance;

    private static Config config;

    private GsonStorageHandler<Map<UUID, Hunter>> hunterStorage;
    private GsonStorageHandler<Map<UUID, Bounty>> bountyStorage;

    public static Config getConfiguration() {
        return config;
    }

    public static BountyPlugin getInstance() {
        return instance;
    }

    @Override
    protected void enable() {
        instance = this;

        config = new Config(this);

        new VaultHook(this);

        loadStorage();

        //Events
        bindModule(new PlayerListener(getConfiguration()));

    }

    private void loadStorage() {
        bountyStorage = new GsonStorageHandler<>("bounties", ".json", getDataFolder(), new TypeToken<Map<UUID, Bounty>>() {
        });

        hunterStorage = new GsonStorageHandler<>("hunters", ".json", getDataFolder(), new TypeToken<Map<UUID, Hunter>>() {
        });


        this.getLogger().info("Loading Bounties...");

        bountyStorage.load().ifPresent(data -> data.forEach((uuid, bounty) -> {
            Bukkit.getLogger().info("Loaded bounty [uuid] -> " + uuid);
            BountiesApi.getBountyRegistry().register(uuid, bounty);
        }));

        this.getLogger().info("Loaded " + BountiesApi.getBounties().size() + " bounties.");

        this.getLogger().info("Loading Hunters...");


        hunterStorage.load().ifPresent(data -> data.forEach((uuid, hunter) -> {
            this.getLogger().info("Loaded hunter [uuid] -> " + uuid);
            BountiesApi.getHunterRegistry().register(uuid, hunter);
        }));
        this.getLogger().info("Loaded " + BountiesApi.getHunters().size() + " hunters.");

        loadCommand();

    }

    private void loadCommand() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new BountyCommand(config));
    }

    public void save() {
        bountyStorage.save(BountiesApi.getBounties());
        hunterStorage.save(BountiesApi.getHunters());
    }

    @Override
    protected void disable() {
        save();
    }

}
