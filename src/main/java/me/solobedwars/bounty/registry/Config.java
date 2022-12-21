package me.solobedwars.bounty.registry;

import me.lucko.helper.text3.Text;
import me.solobedwars.bounty.api.Placeholder;
import me.solobedwars.bounty.plugin.BountyPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Config {

    private static BountyPlugin plugin;
    //blacklisted-worlds
    public Set<String> WHITELISTED_WORLDS;
    public String PREFIX;
    //Bounty
    public double BOUNTY_MINIMUM_REWARD;
    public int BOUNTY_COOLDOWN;
    public String BOUNTY_SKULL_NAME;
    public List<String> BOUNTY_SKULL_LORE;
    public double BOUNTY_TAX_VOID;
    public double BOUNTY_TAX_RETURN;
    public double BOUNTY_TAX_MAX_RETURN;
    //Warden
    public Set<Integer> WARDEN_IDS;
    public String WARDEN_STOP_BOTHERING;
    public String WARDEN_EXPIRED_HEAD;
    public List<String> WARDEN_BOUNTY_CLAIM;
    //Menu
    public String MENU_NAME;
    // - Info
    public String MENU_INFO_NAME;
    public List<String> MENU_INFO_LORE;
    // - Top
    public String MENU_TOP_NAME;
    public List<String> MENU_TOP_LORE;
    // - Bounty
    public String MENU_BOUNTY_NAME;
    public List<String> MENU_BOUNTY_LORE;

    public Config(BountyPlugin plugin) {
        Config.plugin = plugin;
        plugin.getConfig().options().copyDefaults();
        plugin.saveDefaultConfig();

        setup();
    }

    public static List<String> replacePlaceholders(List<String> input, Set<Placeholder> placeholders) {

        List<String> strings = new ArrayList<>();

        for (String s : input) {
            strings.add(replacePlaceholders(s, placeholders));
        }

        return strings;
    }

    public static String replacePlaceholders(@NotNull String input, Placeholder placeholder) {
        return Text.colorize(placeholder.replace(input));
    }

    public static String replacePlaceholders(@NotNull String input, Set<Placeholder> placeholders) {
        for (Placeholder placeholder : placeholders) {
            input = replacePlaceholders(input, placeholder);
        }

        return input;
    }

    public void setup() {

        WHITELISTED_WORLDS = getStringSet("blacklisted-worlds");
        PREFIX = getString("prefix");

        BOUNTY_MINIMUM_REWARD = getDouble("bounty.minimum-reward");
        BOUNTY_COOLDOWN = getInt("bounty.cooldown");

        BOUNTY_SKULL_NAME = getString("bounty.skull", "name");
        BOUNTY_SKULL_LORE = getStringList("bounty.skull", "lore");

        BOUNTY_TAX_VOID = getDouble("bounty.tax", "void");
        BOUNTY_TAX_RETURN = getDouble("bounty.tax", "return-on-hunter");
        BOUNTY_TAX_MAX_RETURN = getDouble("bounty.tax", "max-return");

        WARDEN_IDS = getIntSet("warden.ids");
        WARDEN_STOP_BOTHERING = getString("warden.warden-stop-bothering");
        WARDEN_EXPIRED_HEAD = getString("warden.warden-expired-head");
        WARDEN_BOUNTY_CLAIM = getStringList("warden.warden-bounty-claim");

        MENU_NAME = getString("menu.name");
        MENU_INFO_NAME = getString("menu.info", "name");
        MENU_INFO_LORE = getStringList("menu.info", "lore");

        MENU_TOP_NAME = getString("menu", "top", "name");
        MENU_TOP_LORE = getStringList("menu", "top", "lore");

        MENU_BOUNTY_NAME = getString("menu", "bounty", "name");
        MENU_BOUNTY_LORE = getStringList("menu", "bounty", "lore");
    }

    private String getString(String... path) {
        return plugin.getConfig().getString(String.join(".", path));
    }

    private double getDouble(String... path) {
        return plugin.getConfig().getDouble(String.join(".", path));
    }

    private int getInt(String... path) {
        return plugin.getConfig().getInt(String.join(".", path));
    }

    private List<Integer> getIntList(String... path) {
        return plugin.getConfig().getIntegerList(String.join(".", path));
    }

    private List<String> getStringList(String... path) {
        return plugin.getConfig().getStringList(String.join(".", path));
    }

    private List<Double> getDoubleList(String... path) {
        return plugin.getConfig().getDoubleList(String.join(".", path));
    }

    private Set<String> getStringSet(String... path) {
        return new HashSet<>(plugin.getConfig().getStringList(String.join(".", path)));
    }

    private Set<Integer> getIntSet(String... path) {
        return new HashSet<>(plugin.getConfig().getIntegerList(String.join(".", path)));
    }

}