package net.mrmisc.essenceofthewild.worldgen.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class EOTWBiomeTags {
    public static final TagKey<Biome> BLACK_FOX_SPAWNS =
            TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "black_fox_spawns"));
    public static final TagKey<Biome> MANGO_TREE_BIOMES =
            TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "mango_tree_biomes"));

}
