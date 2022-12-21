package me.solobedwars.bounty.factory;

import com.google.common.collect.ImmutableSet;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.solobedwars.bounty.api.Bounty;
import me.solobedwars.bounty.api.Placeholder;
import me.solobedwars.bounty.plugin.BountyPlugin;
import me.solobedwars.bounty.registry.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;
import java.util.Set;

public class SkullFactory {

    public static ItemStack make(@NotNull Player player, @NotNull Bounty bounty) {
        Config config = BountyPlugin.getConfiguration();

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setOwner(bounty.getTargetName());

        Set<Placeholder> placeholders = ImmutableSet.of(
                Placeholder.of("target", bounty.getTargetName()),
                Placeholder.of("player", player.getName())
        );

        skullMeta.setDisplayName(Config.replacePlaceholders(config.BOUNTY_SKULL_NAME, placeholders));

        final List<String> lore = Config.replacePlaceholders(config.BOUNTY_SKULL_LORE, placeholders);

        skullMeta.setLore(lore);

        skull.setItemMeta(skullMeta);

        NBTItem nbt = new NBTItem(skull);

        NBTCompound compound = nbt.getOrCreateCompound("bounty");

        compound.setUUID("uuid", bounty.getTarget());
        compound.setLong("lastDeath", bounty.getLastDeath());


        return nbt.getItem();
    }


    public static ItemStack makeListing(@NotNull Bounty bounty) {

        Config config = BountyPlugin.getConfiguration();

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        Set<Placeholder> placeholders = ImmutableSet.of(
                Placeholder.of("player", bounty.getTargetName()),
                Placeholder.of("reward", String.valueOf(bounty.getReward())),
                Placeholder.of("formatted_reward", NumberFormat.getInstance().format(bounty.getReward())),
                Placeholder.of("contributors", NumberFormat.getInstance().format(bounty.getContributors().size()))
        );

        skullMeta.setOwner(bounty.getTargetName());

        skullMeta.setDisplayName(Config.replacePlaceholders(config.MENU_BOUNTY_NAME, placeholders));

        final List<String> lore = Config.replacePlaceholders(config.MENU_BOUNTY_LORE, placeholders);

        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);

        return skull;
    }


}
