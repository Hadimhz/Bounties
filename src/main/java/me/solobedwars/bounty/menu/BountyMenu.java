package me.solobedwars.bounty.menu;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.paginated.PaginatedGui;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.menu.scheme.StandardSchemeMappings;
import me.lucko.helper.text3.Text;
import me.solobedwars.bounty.api.BountiesApi;
import me.solobedwars.bounty.plugin.BountyPlugin;
import me.solobedwars.bounty.registry.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class BountyMenu extends PaginatedGui {

    private static final MenuScheme BUTTONS = new MenuScheme().mask("000101000");


    private static final MenuScheme STAINED_SCHEME = new MenuScheme(StandardSchemeMappings.STAINED_GLASS)
            .masks("111010111",
                    "100000001",
                    "100000001",
                    "100000001",
                    "100000001",
                    "111111111")
            .scheme(14, 14, 14, 14, 14, 14, 14, 14, 14)
            .scheme(14, 14)
            .scheme(14, 14)
            .scheme(14, 14)
            .scheme(14, 14)
            .scheme(14, 14, 14, 14, 14, 14, 14, 14, 14);

    private static final List<Integer> ITEM_SLOTS = new MenuScheme()
            .maskEmpty(1)
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .getMaskedIndexesImmutable();

    private static final Config config = BountyPlugin.getConfiguration();


    public BountyMenu(Player player) {
        super(gui -> BountiesApi.getOrderedBountiesSkulls(),
                player,
                PaginatedGuiBuilder.create()
                        .title(Text.colorize(config.MENU_NAME))
                        .lines(6)
                        .scheme(STAINED_SCHEME)
                        .itemSlots(ITEM_SLOTS)
                        .nextPageSlot(53)
                        .previousPageSlot(45)
        );

        MenuPopulator populator = BUTTONS.newPopulator(this);

        //INFO
        populator.accept(ItemStackBuilder.of(Material.COMPASS).name(config.MENU_INFO_NAME).lore(config.MENU_INFO_LORE).buildItem().build());

        populator.accept(ItemStackBuilder.of(Material.RABBIT_HIDE).name(config.MENU_TOP_NAME).lore(config.MENU_TOP_LORE).buildItem().build());


    }
}
