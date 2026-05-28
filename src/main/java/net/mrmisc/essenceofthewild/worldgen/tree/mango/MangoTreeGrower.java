package net.mrmisc.essenceofthewild.worldgen.tree.mango;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.mrmisc.essenceofthewild.worldgen.datagen.EOTWConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class MangoTreeGrower extends AbstractMegaTreeGrower {
    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return null;
    }


    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource pRandom) {
        return EOTWConfiguredFeatures.MANGO_KEY;
    }
}
