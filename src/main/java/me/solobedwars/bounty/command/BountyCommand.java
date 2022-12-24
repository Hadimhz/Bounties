package me.solobedwars.bounty.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.collect.ImmutableSet;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.lucko.helper.Schedulers;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.api.Bounty;
import me.solobedwars.bounty.api.Placeholder;
import me.solobedwars.bounty.hook.VaultHook;
import me.solobedwars.bounty.menu.BountyMenu;
import me.solobedwars.bounty.plugin.BountyPlugin;
import me.solobedwars.bounty.registry.Config;
import me.solobedwars.bounty.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;

@CommandAlias("bounty")
public class BountyCommand extends BaseCommand {
    private final NumberFormat format;
    private final Config config;

    public BountyCommand(Config config) {
        this.format = NumberFormat.getInstance();
        this.config = config;
    }


    @Default
    public void onMenu(Player sender) {

        new BountyMenu(sender).open();
    }

    @Subcommand("add")
    @CommandPermission("bounty.add")
    @Syntax("<player> <money>")
    @CommandCompletion("@players 1")
    public void onBountyAdd(Player sender, String player, double money) {

        Schedulers.async().run(() -> {

            if (Objects.equals(sender.getName(), player)) {
                sender.sendMessage(Chat.colorFormat("%s&cYou can't put a bounty on yourself&c.", config.PREFIX));
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(player);

            if (target == null) {
                sender.sendMessage(Chat.colorFormat("%s&cCouldn't find a player with the name &f%s&c.", config.PREFIX, player));
                return;
            }

            if (money < config.BOUNTY_MINIMUM_REWARD) {
                sender.sendMessage(Chat.colorFormat("%s&cBounty has to be at least $%s.", config.PREFIX, format.format(config.BOUNTY_MINIMUM_REWARD)));
                return;
            }

            if (VaultHook.getEconomy().getBalance(sender) < money) {
                sender.sendMessage(Chat.colorFormat("%s&cInsufficient balance.", config.PREFIX));
                return;
            }

            Bounty bounty = BountiesApi.getOrCreateBounty(target.getUniqueId());

            VaultHook.getEconomy().withdrawPlayer(sender, money);

            bounty.addContribution(sender.getUniqueId(), money);

            sender.sendMessage(
                    Chat.colorFormat("%s&cBounty on &f%s&c is now &6%s&f!",
                            config.PREFIX,
                            target.getName(),
                            format.format(bounty.getReward())));


            BountyPlugin.getInstance().save();
        });

    }

    @Subcommand("claimfor")
    @Syntax("<player>")
    @CommandCompletion("@players")
    @CommandPermission("bounty.admin")
    public void onBountyClaim(CommandSender sender, String playerName) {
        Schedulers.async().run(() -> {

            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                sender.sendMessage(Chat.colorFormat("%s&cCouldn't find a player with the name &f%s&c.", config.PREFIX, playerName));
                return;
            }

            ItemStack itemStack = player.getItemInHand();


            if (itemStack.getType() != Material.SKULL_ITEM) {
                player.sendMessage(
                        Config.replacePlaceholders(config.WARDEN_STOP_BOTHERING,
                                Placeholder.of("player", player.getName()))
                );
                return;
            }

            NBTItem nbt = new NBTItem(itemStack);

            NBTCompound compound = nbt.getCompound("bounty");

            if (compound == null) {
                player.sendMessage(
                        Config.replacePlaceholders(config.WARDEN_STOP_BOTHERING,
                                Placeholder.of("player", player.getName()))
                );
                return;
            }
            long time = System.currentTimeMillis();

            player.getInventory().setItemInHand(new ItemStack(Material.AIR));

            BountiesApi.getBountyFrom(compound.getUUID("uuid"))
                    .ifPresentOrElse(bounty -> {

                        if (bounty.getTarget() == player.getUniqueId()) {
                            player.sendMessage(Config.replacePlaceholders(config.WARDEN_OWN_HEAD,
                                    Placeholder.of("player", player.getName())));
                            return;
                        }

                        if (time > (compound.getLong("lastDeath") + (config.BOUNTY_COOLDOWN * 1000L))) {
                            player.sendMessage(
                                    Config.replacePlaceholders(config.WARDEN_EXPIRED_HEAD,
                                            Placeholder.of("player", player.getName()))
                            );

                            return;
                        }

                        final double voidTax = bounty.getReward() * (config.BOUNTY_TAX_VOID / 100);
                        final double hunterTax = bounty.getReward() * (config.BOUNTY_TAX_RETURN / 100);
                        final double taxed = bounty.getReward() - (voidTax + hunterTax);

                        player.sendMessage(Config.replacePlaceholders(
                                config.WARDEN_BOUNTY_CLAIM,
                                ImmutableSet.of(
                                        Placeholder.of("player", player.getName()),
                                        Placeholder.of("target", bounty.getTargetName()),
                                        Placeholder.of("reward", bounty.getReward() + ""),
                                        Placeholder.of("formatted_reward", format.format(bounty.getReward())),
                                        Placeholder.of("taxed", taxed + ""),
                                        Placeholder.of("formatted_taxed", format.format(taxed))
                                )
                        ).toArray(new String[0]));

                        BountiesApi.getOrCreateHunter(player.getUniqueId()).claimBounty(bounty);

                        BountiesApi.getBountyRegistry().unRegister(bounty.getTarget());

                        if (hunterTax >= config.BOUNTY_MINIMUM_REWARD) {
                            BountiesApi.getOrCreateBounty(player.getUniqueId())
                                    .addContribution(player.getUniqueId(), Math.min(hunterTax, config.BOUNTY_TAX_MAX_RETURN));
                        }

                        VaultHook.getEconomy().depositPlayer(player, taxed);


                    }, () -> player.sendMessage(
                            Config.replacePlaceholders(config.WARDEN_EXPIRED_HEAD,
                                    Placeholder.of("player", player.getName()))
                    ));
        });
    }


    @Subcommand("reload")
    @CommandPermission("bounty.admin")
    public void onBountyReload(CommandSender sender) {
        Schedulers.async().run(() -> {

            BountyPlugin.getInstance().reloadConfig();
            sender.sendMessage(Chat.colorFormat("%s&fReloaded config!", config.PREFIX));

            config.setup();

            BountyPlugin.getInstance().getLogger().info("Reloaded config file!");

            BountyPlugin.getInstance().save();
        });
    }

    @Subcommand("debug")
    @CommandPermission("relrubies.admin")
    public void onBountydebug(Player sender) {

        NBTItem item = new NBTItem(sender.getItemInHand());

        sender.sendMessage(item.toString());

    }

    @Subcommand("check")
    @CommandPermission("relrubies.admin")
    @Syntax("<player>")
    @CommandCompletion("@players")
    public void onBountyCheck(Player sender, String player) {
        Schedulers.async().run(() -> {

            OfflinePlayer target = Bukkit.getPlayer(player);

            if (target == null) {
                sender.sendMessage(Chat.colorFormat("%s&cCouldn't find a player with the name &f%s&c.", config.PREFIX, player));
                return;
            }


            Optional<Bounty> optionalBounty = BountiesApi.getBountyFrom(target.getUniqueId());

            optionalBounty.ifPresentOrElse(
                    bounty -> sender.sendMessage(Chat.colorFormat("%s&f%s has a &6$&e%s &fbounty on his head!", config.PREFIX, target.getName(), format.format(bounty.getReward()), format.format(bounty.getContributors().size()))),
                    () -> sender.sendMessage(Chat.colorFormat("%s&cCouldn't find a bounty on %s", config.PREFIX, target.getName()))
            );


        });
    }

}
