package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.worldgen.datagen.EOTWBiomeModifiers;
import net.mrmisc.essenceofthewild.worldgen.datagen.EOTWConfiguredFeatures;
import net.mrmisc.essenceofthewild.worldgen.datagen.EOTWPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EOTWWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, EOTWConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, EOTWPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, EOTWBiomeModifiers::bootstrap);

    public EOTWWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EssenceOfTheWildMod.MOD_ID));
    }
}
