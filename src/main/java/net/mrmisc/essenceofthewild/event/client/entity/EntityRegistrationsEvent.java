package net.mrmisc.essenceofthewild.event.client.entity;

import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntityRenderer;
import net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.client.SleepingBagRenderer;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenModel;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckModel;
import net.mrmisc.essenceofthewild.entity.custom.duck.DucklingModel;
import net.mrmisc.essenceofthewild.entity.custom.ferret.BabyFerretModel;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretModel;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareEntity;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareModel;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.BabyPigModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigSaddleModel;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.BabyRatModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatCollarModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatModel;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistrationsEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PigModel.LAYER_LOCATION, PigModel::createBodyLayer);
        event.registerLayerDefinition(BabyPigModel.LAYER_LOCATION, BabyPigModel::createBodyLayer);
        event.registerLayerDefinition(PigSaddleModel.LAYER_LOCATION, PigSaddleModel::createBodyLayer);
        event.registerLayerDefinition(ChickenModel.LAYER_LOCATION, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(DuckModel.LAYER_LOCATION, DuckModel::createBodyLayer);
        event.registerLayerDefinition(DucklingModel.LAYER_LOCATION, DucklingModel::createBodyLayer);
        event.registerLayerDefinition(RabbitModel.LAYER_LOCATION, RabbitModel::createBodyLayer);
        event.registerLayerDefinition(HareModel.LAYER_LOCATION, HareModel::createBodyLayer);
        event.registerLayerDefinition(SleepingBagRenderer.HEAD, SleepingBagRenderer::createHeadLayer);
        event.registerLayerDefinition(SleepingBagRenderer.FOOT, SleepingBagRenderer::createFootLayer);
        event.registerLayerDefinition(FerretModel.LAYER_LOCATION, FerretModel::createBodyLayer);
        event.registerLayerDefinition(BabyFerretModel.LAYER_LOCATION, BabyFerretModel::createBodyLayer);
        event.registerLayerDefinition(RatModel.LAYER_LOCATION, RatModel::createBodyLayer);
        event.registerLayerDefinition(BabyRatModel.LAYER_LOCATION, BabyRatModel::createBodyLayer);
        event.registerLayerDefinition(RatCollarModel.LAYER_LOCATION, RatCollarModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EOTWEntities.SHEEP.get(), SheepEntity.createAttributes().build());
        event.put(EOTWEntities.PIG.get(), PigEntity.createAttributes().build());
        event.put(EOTWEntities.COW.get(), CowEntity.createAttributes().build());
        event.put(EOTWEntities.MOOSHROOM.get(), MooshroomEntity.createAttributes().build());
        event.put(EOTWEntities.CHICKEN.get(), ChickenEntity.createAttributes().build());
        event.put(EOTWEntities.DUCK.get(), DuckEntity.createAttributes().build());
        event.put(EOTWEntities.RABBIT.get(), RabbitEntity.createAttributes().build());
        event.put(EOTWEntities.HARE.get(), HareEntity.createAttributes().build());
        event.put(EOTWEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.put(EOTWEntities.RAT.get(), RatEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EOTWBlockEntities.NEST.get(), NestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(EOTWBlockEntities.SLEEPING_BAG.get(), SleepingBagRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        NestBlockEntityRenderer.registerAdditionalModels(event);
    }
}
