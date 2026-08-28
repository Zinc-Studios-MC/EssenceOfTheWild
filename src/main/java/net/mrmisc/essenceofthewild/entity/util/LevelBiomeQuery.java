package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;

public final class LevelBiomeQuery implements BiomeQuery {
    private final Holder<Biome> biome;

    public LevelBiomeQuery(LevelReader level, BlockPos pos) {
        this.biome = level.getBiome(pos);
    }

    @Override
    public boolean is(Habitat habitat) {
        return switch (habitat) {
            case COLD -> biome.is(Tags.Biomes.IS_COLD);
            case HOT -> biome.is(Tags.Biomes.IS_HOT);
            case PLAINS -> biome.is(Tags.Biomes.IS_PLAINS);
            case RIVER -> biome.is(BiomeTags.IS_RIVER);
            case DESERT -> biome.is(Biomes.DESERT);
            case SWAMP -> biome.is(Biomes.SWAMP);
            case MANGROVE_SWAMP -> biome.is(Biomes.MANGROVE_SWAMP);
            case JUNGLE -> biome.is(Biomes.JUNGLE);
            case BAMBOO_JUNGLE -> biome.is(Biomes.BAMBOO_JUNGLE);
            case SPARSE_JUNGLE -> biome.is(Biomes.SPARSE_JUNGLE);
            case FOREST -> biome.is(Biomes.FOREST);
            case BIRCH_FOREST -> biome.is(Biomes.BIRCH_FOREST);
            case FLOWER_FOREST -> biome.is(Biomes.FLOWER_FOREST);
            case TAIGA -> biome.is(Biomes.TAIGA);
            case SNOWY_TAIGA -> biome.is(Biomes.SNOWY_TAIGA);
            case OLD_GROWTH_SPRUCE_TAIGA -> biome.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
            case OLD_GROWTH_PINE_TAIGA -> biome.is(Biomes.OLD_GROWTH_PINE_TAIGA);
        };
    }
}
