package net.mrmisc.essenceofthewild.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.cave_spider.CaveSpiderEntity;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;
import net.mrmisc.essenceofthewild.entity.custom.spider.SpiderEntity;
import net.mrmisc.essenceofthewild.entity.misc.SilkBall;
import net.mrmisc.essenceofthewild.entity.misc.ThrownDuckEgg;
import net.mrmisc.essenceofthewild.entity.misc.arrow.UnderwaterArrow;

public class EOTWEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<EntityType<SheepEntity>> SHEEP = ENTITIES.register("sheep",
            ()-> EntityType.Builder.of(SheepEntity::new, MobCategory.CREATURE).sized(1f, 1.2f).build("sheep"));
    public static RegistryObject<EntityType<CowEntity>> COW = ENTITIES.register("cow",
            ()-> EntityType.Builder.of(CowEntity::new, MobCategory.CREATURE).sized(1f, 1.2f).build("cow"));
    public static RegistryObject<EntityType<MooshroomEntity>> MOOSHROOM = ENTITIES.register("mooshroom",
            ()-> EntityType.Builder.of(MooshroomEntity::new, MobCategory.CREATURE).sized(1f, 1.2f).build("mooshroom"));
    public static RegistryObject<EntityType<ChickenEntity>> CHICKEN = ENTITIES.register("chicken",
            ()-> EntityType.Builder.of(ChickenEntity::new, MobCategory.CREATURE).sized(0.7f, 0.5f).build("chicken"));
    public static RegistryObject<EntityType<DuckEntity>> DUCK = ENTITIES.register("duck",
            ()-> EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE).sized(0.7f, 0.6f).build("duck"));
    public static RegistryObject<EntityType<PigEntity>> PIG = ENTITIES.register("pig",
            ()-> EntityType.Builder.of(PigEntity::new, MobCategory.CREATURE).sized(0.6f, 1.2f).build("pig"));
    public static RegistryObject<EntityType<RabbitEntity>> RABBIT = ENTITIES.register("rabbit",
            ()-> EntityType.Builder.of(RabbitEntity::new, MobCategory.CREATURE).sized(0.6f, 0.6f).build("rabbit"));
    public static RegistryObject<EntityType<HareEntity>> HARE = ENTITIES.register("hare",
            ()-> EntityType.Builder.of(HareEntity::new, MobCategory.CREATURE).sized(0.6f, 0.6f).build("hare"));
    public static RegistryObject<EntityType<FerretEntity>> FERRET = ENTITIES.register("ferret",
            ()-> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.8f, 0.8f).build("ferret"));
    public static RegistryObject<EntityType<RatEntity>> RAT = ENTITIES.register("rat",
            ()-> EntityType.Builder.of(RatEntity::new, MobCategory.CREATURE).sized(0.6f, 0.5f).build("rat"));
    public static RegistryObject<EntityType<SpiderEntity>> SPIDER = ENTITIES.register("spider",
            ()-> EntityType.Builder.of(SpiderEntity::new, MobCategory.MONSTER).sized(2.2f, 1.6f).clientTrackingRange(8).build("spider"));
    public static RegistryObject<EntityType<CaveSpiderEntity>> CAVE_SPIDER = ENTITIES.register("cave_spider",
            ()-> EntityType.Builder.of(CaveSpiderEntity::new, MobCategory.MONSTER).sized(1.25f, 0.75f).clientTrackingRange(8).build("cave_spider"));
    public static RegistryObject<EntityType<SilkBall>> SILK_BALL = ENTITIES.register("silk_ball",
            ()-> EntityType.Builder.<SilkBall>of(SilkBall::new, MobCategory.MISC).sized(0.35f, 0.35f).clientTrackingRange(4).updateInterval(10).build("silk_ball"));
    public static RegistryObject<EntityType<UnderwaterArrow>> UNDERWATER_ARROW = ENTITIES.register("underwater_arrow",
            ()-> EntityType.Builder.<UnderwaterArrow>of(UnderwaterArrow::new, MobCategory.MISC).sized(0.5f, 0.9f).clientTrackingRange(5).build("underwater_arrow"));
    public static RegistryObject<EntityType<ThrownDuckEgg>> THROWN_DUCK_EGG = ENTITIES.register("thrown_duck_egg",
            ()-> EntityType.Builder.<ThrownDuckEgg>of(ThrownDuckEgg::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("thrown_duck_egg"));
}
