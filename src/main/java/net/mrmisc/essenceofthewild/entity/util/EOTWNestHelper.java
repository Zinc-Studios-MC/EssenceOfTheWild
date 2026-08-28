package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.item.EOTWItems;

import java.util.Optional;

public class EOTWNestHelper {
    public static final int NEST_SEARCH_RADIUS = 32;

    public static Optional<BlockPos> findNearestAvailableNest(Level level, BlockPos center, int radius) {
        BlockPos bestPos = null;
        double bestDistance = Double.MAX_VALUE;
        BlockPos from = center.offset(-radius, -radius, -radius);
        BlockPos to = center.offset(radius, radius, radius);

        for (BlockPos pos : BlockPos.betweenClosed(from, to)) {
            if (!level.getBlockState(pos).is(EOTWBlocks.NEST.get())) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof NestBlockEntity nestBlockEntity) || !nestBlockEntity.canAcceptClutch()) {
                continue;
            }

            double distance = pos.distSqr(center);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestPos = pos.immutable();
            }
        }

        return Optional.ofNullable(bestPos);
    }

    public static boolean isUsableNest(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).is(EOTWBlocks.NEST.get())) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof NestBlockEntity nestBlockEntity && nestBlockEntity.canAcceptClutch();
    }

    public static boolean hasEggs(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof NestBlockEntity nestBlockEntity && nestBlockEntity.hasEggs();
    }

    public static boolean placeEggsInNest(Animal parent, BlockPos pos, int eggCount) {
        BlockEntity blockEntity = parent.level().getBlockEntity(pos);
        return blockEntity instanceof NestBlockEntity nestBlockEntity && nestBlockEntity.addClutch(parent, eggCount);
    }

    public static int randomClutchSize(RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 70) {
            return 1;
        }

        return roll < 94 ? 2 : 3;
    }

    public static void dropEggOnGround(Animal parent) {
        parent.spawnAtLocation(parent instanceof DuckEntity ? EOTWItems.DUCK_EGG.get() : Items.EGG);
        playEggPlaced(parent);
    }

    public static void playEggPlaced(Animal parent) {
        parent.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F + 1.0F);
        parent.gameEvent(GameEvent.ENTITY_PLACE);
    }

    public static void tryPlaceNaturalNest(ServerLevelAccessor level, BlockPos center, RandomSource random) {
        if (level instanceof WorldGenRegion) {
            return;
        }

        ServerLevel serverLevel = level.getLevel();
        if (hasNearbyNest(serverLevel, center, 10)) {
            return;
        }

        for (int i = 0; i < 18; i++) {
            int x = center.getX() + random.nextInt(11) - 5;
            int z = center.getZ() + random.nextInt(11) - 5;
            int startY = center.getY() + random.nextInt(5) - 2;

            for (int y = startY + 3; y >= startY - 4; y--) {
                BlockPos pos = new BlockPos(x, y, z);

                if (canPlaceNest(serverLevel, pos)) {
                    serverLevel.setBlock(pos, EOTWBlocks.NEST.get().defaultBlockState(), 3);
                    serverLevel.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.35F, 1.0F + random.nextFloat() * 0.2F);
                    return;
                }
            }
        }
    }

    public static Optional<Vec3> findGuardTarget(Level level, BlockPos nestPos, RandomSource random) {
        double angle = random.nextDouble() * Math.PI * 2.0D;
        double radius = 2.25D + random.nextDouble() * 2.5D;
        BlockPos raw = nestPos.offset((int) Math.round(Math.cos(angle) * radius), 0, (int) Math.round(Math.sin(angle) * radius));

        for (int yOffset = 2; yOffset >= -3; yOffset--) {
            BlockPos ground = raw.offset(0, yOffset, 0);
            BlockPos target = ground.above();

            if (canStandAt(level, target)) {
                return Optional.of(Vec3.atBottomCenterOf(target));
            }
        }

        return Optional.empty();
    }

    private static boolean hasNearbyNest(Level level, BlockPos center, int radius) {
        BlockPos from = center.offset(-radius, -4, -radius);
        BlockPos to = center.offset(radius, 4, radius);

        for (BlockPos pos : BlockPos.betweenClosed(from, to)) {
            if (level.getBlockState(pos).is(EOTWBlocks.NEST.get())) {
                return true;
            }
        }

        return false;
    }

    private static boolean canPlaceNest(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos)
                && !level.getBlockState(pos.below()).isAir()
                && level.getBlockState(pos.below()).getFluidState().isEmpty()
                && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    private static boolean canStandAt(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir()
                && !level.getBlockState(pos.below()).isAir()
                && level.getBlockState(pos.below()).getFluidState().isEmpty();
    }
}
