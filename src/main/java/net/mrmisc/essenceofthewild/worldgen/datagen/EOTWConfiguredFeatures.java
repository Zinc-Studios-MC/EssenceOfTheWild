package net.mrmisc.essenceofthewild.worldgen.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.HangingMangoDecorator;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.MangoTrunkPlacer;

import java.util.List;

public class EOTWConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGO_KEY = registerKey("mango");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context){
        register(context, MANGO_KEY, Feature.TREE,
                mangoTree(BlockStateProvider.simple(EOTWBlocks.MANGO_LOG.get()),
                        EOTWBlocks.MANGO_LEAVES.get().defaultBlockState(),
                        EOTWBlocks.VANILLA_LEAVES.get().defaultBlockState(),
                        new HangingMangoDecorator(10)));
    }

    public static TreeConfiguration mangoTree(BlockStateProvider logProvider,
                                              BlockState primaryLeavesProvider,
                                              BlockState secondaryLeavesProvider,
                                              TreeDecorator hangingMangoDecorator) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                logProvider,
                new MangoTrunkPlacer(8, 0, 0),   // 2x2 trunk, tall and chunky
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(primaryLeavesProvider, 3).add(secondaryLeavesProvider, 1)),
                new BlobFoliagePlacer(
                        ConstantInt.of(3),
                        ConstantInt.of(0),
                        4
                ),
                new TwoLayersFeatureSize(1, 0, 1)
        )
                .dirt(BlockStateProvider.simple(Blocks.DIRT))
                .decorators(List.of(hangingMangoDecorator))
                .ignoreVines()
                .build();
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config){
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
