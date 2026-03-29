package net.mrmisc.essenceofthewild.util;

import net.minecraftforge.eventbus.api.IEventBus;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.tab.EOTWCreativeModeTabs;

public class EOTWUtils {
    public static void init(IEventBus eventBus) {
        EOTWEntities.ENTITIES.register(eventBus);
        EOTWItems.ITEMS.register(eventBus);
        EOTWCreativeModeTabs.TABS.register(eventBus);
    }
}
