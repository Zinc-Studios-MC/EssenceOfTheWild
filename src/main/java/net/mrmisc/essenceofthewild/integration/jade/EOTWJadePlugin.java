package net.mrmisc.essenceofthewild.integration.jade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Animal;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.block.custom.misc.MangoBlock;
import net.mrmisc.essenceofthewild.block.custom.nest.NestBlock;
import net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker.CheeseMakerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class EOTWJadePlugin implements IWailaPlugin {
    public static final ResourceLocation BLOCKS = new ResourceLocation(EssenceOfTheWildMod.MOD_ID, "blocks");
    public static final ResourceLocation ENTITIES = new ResourceLocation(EssenceOfTheWildMod.MOD_ID, "entities");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(EOTWJadeBlockProvider.INSTANCE, WoodenFreezerBlockEntity.class);
        registration.registerBlockDataProvider(EOTWJadeBlockProvider.INSTANCE, CheeseMakerBlockEntity.class);
        registration.registerBlockDataProvider(EOTWJadeBlockProvider.INSTANCE, NestBlockEntity.class);
        registration.registerEntityDataProvider(EOTWJadeEntityProvider.INSTANCE, Animal.class);
        registration.registerEntityDataProvider(EOTWJadeEntityProvider.INSTANCE, ItemEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(EOTWJadeBlockProvider.INSTANCE, WoodenFreezerBlock.class);
        registration.registerBlockComponent(EOTWJadeBlockProvider.INSTANCE, CheeseMakerBlock.class);
        registration.registerBlockComponent(EOTWJadeBlockProvider.INSTANCE, NestBlock.class);
        registration.registerBlockComponent(EOTWJadeBlockProvider.INSTANCE, MangoBlock.class);
        registration.registerEntityComponent(EOTWJadeEntityProvider.INSTANCE, Animal.class);
        registration.registerEntityComponent(EOTWJadeEntityProvider.INSTANCE, ItemEntity.class);
    }
}
