package net.mrmisc.essenceofthewild.util;

import net.minecraftforge.eventbus.api.IEventBus;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.tab.EOTWCreativeModeTabs;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTreeDecorators;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTrunkPlacers;

public class EOTWUtils {
    public static void init(IEventBus eventBus) {
        EOTWEntities.ENTITIES.register(eventBus);
        EOTWItems.ITEMS.register(eventBus);
        EOTWBlocks.BLOCKS.register(eventBus);
        EOTWBlockEntities.BLOCK_ENTITY.register(eventBus);
        EOTWEffects.EFFECTS.register(eventBus);
        EOTWCreativeModeTabs.TABS.register(eventBus);
        EOTWTreeDecorators.TREE_DECORATORS.register(eventBus);
        EOTWTrunkPlacers.TRUNK_PLACERS.register(eventBus);
    }
}
