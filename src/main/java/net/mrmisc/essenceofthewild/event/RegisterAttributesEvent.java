package net.mrmisc.essenceofthewild.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepModel;
import net.mrmisc.essenceofthewild.entity.custom.sheep.WoolModel;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterAttributesEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SheepModel.LAYER_LOCATION, SheepModel::createBodyLayer);
        event.registerLayerDefinition(WoolModel.LAYER_LOCATION, WoolModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EOTWEntities.SHEEP.get(), SheepEntity.createAttributes().build());
    }
}
