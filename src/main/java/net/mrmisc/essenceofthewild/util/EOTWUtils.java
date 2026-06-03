package net.mrmisc.essenceofthewild.util;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.config.EOTWConfig;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.menu.EOTWMenuTypes;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;
import net.mrmisc.essenceofthewild.tab.EOTWCreativeModeTabs;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTreeDecorators;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTrunkPlacers;

public class EOTWUtils {

    public static final BlockSetType MANGO = new BlockSetType("mango");
    public static final WoodType MANGO_WOOD = WoodType.register(new WoodType("mango", MANGO));
    public static void modInit(IEventBus eventBus) {
        EOTWEntities.ENTITIES.register(eventBus);
        EOTWItems.ITEMS.register(eventBus);
        EOTWBlocks.BLOCKS.register(eventBus);
        EOTWBlockEntities.BLOCK_ENTITY.register(eventBus);
        EOTWMenuTypes.MENU_TYPES.register(eventBus);
        EOTWRecipes.RECIPE_TYPES.register(eventBus);
        EOTWRecipes.RECIPE_SERIALIZERS.register(eventBus);
        EOTWEffects.EFFECTS.register(eventBus);
        EOTWCreativeModeTabs.TABS.register(eventBus);
        EOTWTreeDecorators.TREE_DECORATORS.register(eventBus);
        EOTWTrunkPlacers.TRUNK_PLACERS.register(eventBus);
    }

    public static void clientInit(){
        BlockSetType.register(MANGO);
        Sheets.addWoodType(MANGO_WOOD);
    }

    public static ResourceLocation getLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);
    }
}
