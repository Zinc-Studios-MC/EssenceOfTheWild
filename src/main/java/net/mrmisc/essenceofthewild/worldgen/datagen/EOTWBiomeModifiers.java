package net.mrmisc.essenceofthewild.worldgen.datagen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.worldgen.util.EOTWBiomeTags;

public class EOTWBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MANGO_TREES = registerKey("add_mango_trees");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(
                ADD_MANGO_TREES,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(EOTWBiomeTags.MANGO_TREE_BIOMES),
                        HolderSet.direct(placedFeatures.getOrThrow(EOTWPlacedFeatures.MANGO_PLACED_KEY)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, name));
    }
}
