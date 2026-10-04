package net.mrmisc.essenceofthewild.event.mod;

import net.mrmisc.essenceofthewild.entity.custom.warthog.WarthogEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EOTWEntities.SHEEP.get(), SheepEntity.createAttributes().build());
        event.put(EOTWEntities.WARTHOG.get(), WarthogEntity.createAttributes().build());
        event.put(EOTWEntities.PIG.get(), PigEntity.createAttributes().build());
        event.put(EOTWEntities.COW.get(), CowEntity.createAttributes().build());
        event.put(EOTWEntities.MOOSHROOM.get(), MooshroomEntity.createAttributes().build());
        event.put(EOTWEntities.CHICKEN.get(), ChickenEntity.createAttributes().build());
        event.put(EOTWEntities.DUCK.get(), DuckEntity.createAttributes().build());
        event.put(EOTWEntities.RABBIT.get(), RabbitEntity.createAttributes().build());
        event.put(EOTWEntities.HARE.get(), HareEntity.createAttributes().build());
        if (EOTWEntities.FERRET.isPresent()) {
            event.put(EOTWEntities.FERRET.get(), FerretEntity.createAttributes().build());
        }
        event.put(EOTWEntities.RAT.get(), RatEntity.createAttributes().build());
        event.put(EOTWEntities.SPIDER.get(), Spider.createAttributes().build());
        event.put(EOTWEntities.CAVE_SPIDER.get(), CaveSpider.createCaveSpider().build());
    }
}
