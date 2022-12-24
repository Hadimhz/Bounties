package me.solobedwars.bounty.menu;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.menu.scheme.StandardSchemeMappings;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.plugin.BountyPlugin;
import me.solobedwars.bounty.registry.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class HuntersTopMenu extends Gui {
    private static final Config config = BountyPlugin.getConfiguration();

    private static final MenuScheme STAINED_SCHEME = new MenuScheme(StandardSchemeMappings.STAINED_GLASS)
            .masks("111111111",
                    "111101111",
                    "111010111",
                    "110101011",
                    "101010101",
                    "111111111")
            .scheme(14, 14, 14, 14, 14, 14, 14, 14, 14)
            .scheme(14, 14, 14, 14, 14, 14, 14, 14)
            .scheme(14, 14, 14, 14, 14, 14, 14)
            .scheme(14, 14, 14, 14, 14, 14)
            .scheme(14, 14, 14, 14, 14)
            .scheme(14, 14, 14, 14, 14, 14, 14, 14, 14);


    private static final Item INVALID = ItemStackBuilder.of(Material.BARRIER).name("&cNone").buildItem().build();


    private static final MenuScheme SKULLS = new MenuScheme()
            .maskEmpty(1)
            .mask("000010000")
            .mask("000101000")
            .mask("001010100")
            .mask("010101010");

    public HuntersTopMenu(Player player) {
        super(player, 6, config.MENU_TOP_NAME);
    }

    @Override
    public void redraw() {

        STAINED_SCHEME.apply(this);


        MenuPopulator populator = SKULLS.newPopulator(this);

        List<Item> heads = BountiesApi.getOrderedHuntersSkulls();

        for (int i = 0; i < 10; i++) {
            if (i + 1 <= heads.size())
                populator.accept(heads.get(i));
            else populator.accept(INVALID);
        }

    }

}
