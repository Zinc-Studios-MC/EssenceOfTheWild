package net.mrmisc.essenceofthewild.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenModel;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowModel;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigModel;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigSaddleModel;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepModel;
import net.mrmisc.essenceofthewild.entity.custom.sheep.WoolModel;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistrationsEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SheepModel.LAYER_LOCATION, SheepModel::createBodyLayer);
        event.registerLayerDefinition(WoolModel.LAYER_LOCATION, WoolModel::createBodyLayer);
        event.registerLayerDefinition(PigModel.LAYER_LOCATION, PigModel::createBodyLayer);
        event.registerLayerDefinition(PigSaddleModel.LAYER_LOCATION, PigSaddleModel::createBodyLayer);
        event.registerLayerDefinition(CowModel.LAYER_LOCATION, CowModel::createBodyLayer);
        event.registerLayerDefinition(MooshroomModel.LAYER_LOCATION, MooshroomModel::createBodyLayer);
        event.registerLayerDefinition(ChickenModel.LAYER_LOCATION, ChickenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EOTWEntities.SHEEP.get(), SheepEntity.createAttributes().build());
        event.put(EOTWEntities.PIG.get(), PigEntity.createAttributes().build());
        event.put(EOTWEntities.COW.get(), CowEntity.createAttributes().build());
        event.put(EOTWEntities.MOOSHROOM.get(), MooshroomEntity.createAttributes().build());
        event.put(EOTWEntities.CHICKEN.get(), ChickenEntity.createAttributes().build());
    }
}
