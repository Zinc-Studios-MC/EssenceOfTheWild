package net.mrmisc.essenceofthewild.worldgen.tree.mango;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.mrmisc.essenceofthewild.worldgen.registry.EOTWTrunkPlacers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MangoTrunkPlacer extends TrunkPlacer {
    public static final Codec<MangoTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance).apply(instance, MangoTrunkPlacer::new)
    );

    public MangoTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return EOTWTrunkPlacers.MANGO.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> placer,
            RandomSource random,
            int height,
            BlockPos pos,
            TreeConfiguration config
    ) {
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();

        // ===== 1. Place 2x2 trunk =====
        for (int y = 0; y < height; y++) {
            BlockPos base = pos.above(y);

            placeLog(level, placer, random, base, config);
            placeLog(level, placer, random, base.east(), config);
            placeLog(level, placer, random, base.south(), config);
            placeLog(level, placer, random, base.east().south(), config);
        }

        // Top center of trunk
        BlockPos top = pos.above(height - 1).east().south();

        // Add main top foliage
        attachments.add(new FoliagePlacer.FoliageAttachment(top.above(), 0, false));

        // ===== 2. Generate branches =====
        int branchCount = 3 + random.nextInt(3); // 3–5 main branches

        for (int i = 0; i < branchCount; i++) {

            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int startY = height - 3 - random.nextInt(2);

            BlockPos start = pos.above(startY).east().south();

            generateSplitBranch(level, placer, random, start, dir, config, attachments);
        }

        return attachments;
    }

    private void generateSplitBranch(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> placer,
            RandomSource random,
            BlockPos start,
            Direction dir,
            TreeConfiguration config,
            List<FoliagePlacer.FoliageAttachment> attachments
    ) {
        int mainLength = 2 + random.nextInt(2); // short base

        BlockPos current = start;

        // ===== main stem =====
        for (int i = 0; i < mainLength; i++) {
            current = current.relative(dir);

            if (random.nextBoolean()) {
                current = current.above();
            }

            placeLog(level, placer, random, current, config);
        }

        // ===== split into 2–3 sub-branches =====
        int splits = 2 + random.nextInt(2);

        for (int s = 0; s < splits; s++) {

            Direction newDir = random.nextBoolean()
                    ? dir.getClockWise()
                    : dir.getCounterClockWise();

            // sometimes keep same direction for variation
            if (random.nextInt(3) == 0) {
                newDir = dir;
            }

            int length = 2 + random.nextInt(2);
            int thickness = random.nextBoolean() ? 1 : 2;

            BlockPos branchPos = current;

            for (int j = 0; j < length; j++) {
                branchPos = branchPos.relative(newDir);

                if (random.nextBoolean()) {
                    branchPos = branchPos.above();
                }

                placeThickLog(level, placer, random, branchPos, newDir, thickness, config);
            }

            // foliage at each small branch end
            attachments.add(new FoliagePlacer.FoliageAttachment(branchPos.above(), 0, false));
        }
    }

    private void placeThickLog(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> placer,
            RandomSource random,
            BlockPos pos,
            Direction dir,
            int thickness,
            TreeConfiguration config
    ) {
        placeLog(level, placer, random, pos, config);

        if (thickness == 2) {
            Direction side = dir.getClockWise();

            placeLog(level, placer, random, pos.relative(side), config);

            // optional: make it less flat
            if (random.nextBoolean()) {
                placeLog(level, placer, random, pos.relative(side.getOpposite()), config);
            }
        }
    }

    // ===== Helper: place branch logs =====
    private void placeBranch(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> placer,
            RandomSource random,
            BlockPos pos,
            Direction dir,
            int thickness,
            TreeConfiguration config
    ) {
        placeLog(level, placer, random, pos, config);

        if (thickness == 2) {
            Direction side = dir.getClockWise();

            placeLog(level, placer, random, pos.relative(side), config);
            placeLog(level, placer, random, pos.relative(side.getOpposite()), config);
        }
    }
}
