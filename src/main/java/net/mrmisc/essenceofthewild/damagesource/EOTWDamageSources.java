package net.mrmisc.essenceofthewild.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class EOTWDamageSources {
    public static final ResourceKey<DamageType> RABIES_WATER = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "rabies_water"));
    public static final ResourceKey<DamageType> RABIES_FINAL = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "rabies_final"));

    public static DamageSource rabiesWaterDamage(Entity entity){
        return new DamageSource(entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(RABIES_WATER), entity);
    }public static DamageSource rabiesFinalDamage(Entity entity){
        return new DamageSource(entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(RABIES_FINAL), entity);
    }
}
