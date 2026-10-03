package net.mrmisc.essenceofthewild.event.client.entity;

import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntityRenderer;
import net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.client.SleepingBagRenderer;
import net.mrmisc.essenceofthewild.entity.custom.chicken.BabyChickenModel;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenModel;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckModel;
import net.mrmisc.essenceofthewild.entity.custom.duck.DucklingModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.BabyPigModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigSaddleModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.BabyRatModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatCollarModel;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatModel;
import net.mrmisc.essenceofthewild.effect.client.WebbedLayer;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class EntityRegistrationsEvent {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PigModel.LAYER_LOCATION, PigModel::createBodyLayer);
        event.registerLayerDefinition(BabyPigModel.LAYER_LOCATION, BabyPigModel::createBodyLayer);
        event.registerLayerDefinition(PigSaddleModel.LAYER_LOCATION, PigSaddleModel::createBodyLayer);
        event.registerLayerDefinition(ChickenModel.LAYER_LOCATION, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(BabyChickenModel.LAYER_LOCATION, BabyChickenModel::createBodyLayer);
        event.registerLayerDefinition(DuckModel.LAYER_LOCATION, DuckModel::createBodyLayer);
        event.registerLayerDefinition(DucklingModel.LAYER_LOCATION, DucklingModel::createBodyLayer);
        event.registerLayerDefinition(SleepingBagRenderer.HEAD, SleepingBagRenderer::createHeadLayer);
        event.registerLayerDefinition(SleepingBagRenderer.FOOT, SleepingBagRenderer::createFootLayer);
        event.registerLayerDefinition(RatModel.LAYER_LOCATION, RatModel::createBodyLayer);
        event.registerLayerDefinition(BabyRatModel.LAYER_LOCATION, BabyRatModel::createBodyLayer);
        event.registerLayerDefinition(RatCollarModel.LAYER_LOCATION, RatCollarModel::createBodyLayer);
        event.registerLayerDefinition(WebbedLayer.WIDE_ARMS, () -> WebbedLayer.createLayer(false));
        event.registerLayerDefinition(WebbedLayer.SLIM_ARMS, () -> WebbedLayer.createLayer(true));
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new WebbedLayer(renderer, event.getEntityModels(), "slim".equals(skin)));
            }
        }
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
