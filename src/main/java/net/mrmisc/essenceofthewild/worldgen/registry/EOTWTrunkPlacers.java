package net.mrmisc.essenceofthewild.worldgen.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.MangoTrunkPlacer;

public class EOTWTrunkPlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<TrunkPlacerType<MangoTrunkPlacer>> MANGO = TRUNK_PLACERS.register("mango",
            ()-> new TrunkPlacerType<>(MangoTrunkPlacer.CODEC));
}
