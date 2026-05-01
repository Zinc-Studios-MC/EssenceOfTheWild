package net.mrmisc.essenceofthewild.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomVariants;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;

public class EOTWEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<EntityType<SheepEntity>> SHEEP = ENTITIES.register("sheep",
            ()-> EntityType.Builder.of(SheepEntity::new, MobCategory.AMBIENT).sized(1f, 1.2f).build("sheep"));
    public static RegistryObject<EntityType<CowEntity>> COW = ENTITIES.register("cow",
            ()-> EntityType.Builder.of(CowEntity::new, MobCategory.AMBIENT).sized(1f, 1.2f).build("cow"));
    public static RegistryObject<EntityType<MooshroomEntity>> MOOSHROOM = ENTITIES.register("mooshroom",
            ()-> EntityType.Builder.of(MooshroomEntity::new, MobCategory.AMBIENT).sized(1f, 1.2f).build("mooshroom"));
    public static RegistryObject<EntityType<ChickenEntity>> CHICKEN = ENTITIES.register("chicken",
            ()-> EntityType.Builder.of(ChickenEntity::new, MobCategory.AMBIENT).sized(0.7f, 0.5f).build("chicken"));
    public static RegistryObject<EntityType<PigEntity>> PIG = ENTITIES.register("pig",
            ()-> EntityType.Builder.of(PigEntity::new, MobCategory.AMBIENT).sized(0.6f, 1.2f).build("pig"));
    public static RegistryObject<EntityType<RabbitEntity>> RABBIT = ENTITIES.register("rabbit",
            ()-> EntityType.Builder.of(RabbitEntity::new, MobCategory.AMBIENT).sized(0.6f, 0.6f).build("rabbit"));
    public static RegistryObject<EntityType<HareEntity>> HARE = ENTITIES.register("hare",
            ()-> EntityType.Builder.of(HareEntity::new, MobCategory.AMBIENT).sized(0.6f, 0.6f).build("hare"));
}
