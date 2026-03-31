package net.mrmisc.essenceofthewild.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;

public class EOTWEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<EntityType<SheepEntity>> SHEEP = ENTITIES.register("sheep",
            ()-> EntityType.Builder.of(SheepEntity::new, MobCategory.AMBIENT).sized(1f, 1.2f).build("sheep"));

    public static RegistryObject<EntityType<PigEntity>> PIG = ENTITIES.register("pig",
            ()-> EntityType.Builder.of(PigEntity::new, MobCategory.AMBIENT).sized(0.6f, 1.2f).build("pig"));
}
