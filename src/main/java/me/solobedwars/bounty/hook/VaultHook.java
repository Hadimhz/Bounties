package me.solobedwars.bounty.hook;

import me.solobedwars.bounty.plugin.BountyPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy economy;

    public VaultHook(BountyPlugin plugin) {

        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Disabling: Vault not found.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.getLogger().warning("Disabling: No economy plugin found.");
            return;
        }
        economy = rsp.getProvider();

    }

    public static Economy getEconomy() {
        return economy;
    }


}
