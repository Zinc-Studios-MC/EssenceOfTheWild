package net.mrmisc.essenceofthewild.entity.custom.fox;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

class FoxFollowGoal extends Goal {
    private final FoxEntity fox;
    private LivingEntity owner;
    private int recalc;
    private float waterCost;

    FoxFollowGoal(FoxEntity fox) {
        this.fox = fox;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        owner = fox.getOwner();
        if (owner == null || owner.isSpectator() || fox.isPassenger() || fox.isLeashed()) {
            return false;
        }
        if (!fox.getMainHandItem().isEmpty()) {
            return true;
        }
        return !fox.isOrderedToSit() && fox.getTarget() == null && fox.distanceToSqr(owner) >= 100;
    }

    @Override
    public boolean canContinueToUse() {
        return owner.isAlive() && !owner.isSpectator() && !fox.isPassenger() && !fox.isLeashed()
                && (!fox.getMainHandItem().isEmpty() || (!fox.isOrderedToSit() && fox.getTarget() == null && fox.distanceToSqr(owner) > 4));
    }

    @Override
    public void start() {
        fox.clearStates();
        recalc = 0;
        waterCost = fox.getPathfindingMalus(BlockPathTypes.WATER);
        fox.setPathfindingMalus(BlockPathTypes.WATER, 0);
    }

    @Override
    public void stop() {
        fox.getNavigation().stop();
        fox.setPathfindingMalus(BlockPathTypes.WATER, waterCost);
        owner = null;
    }

    @Override
    public void tick() {
        fox.getLookControl().setLookAt(owner, 10, fox.getMaxHeadXRot());
        if (!fox.getMainHandItem().isEmpty() && fox.distanceToSqr(owner) <= 4) {
            fox.deliver();
            return;
        }
        if (--recalc > 0) {
            return;
        }
        recalc = adjustedTickDelay(10);
        if (fox.distanceToSqr(owner) < 144) {
            fox.getNavigation().moveTo(owner, 1.2);
            return;
        }
        BlockPos base = owner.blockPosition();
        for (int i = 0; i < 10; i++) {
            int x = fox.getRandom().nextInt(7) - 3;
            int y = fox.getRandom().nextInt(3) - 1;
            int z = fox.getRandom().nextInt(7) - 3;
            if (Math.abs(x) < 2 && Math.abs(z) < 2) {
                continue;
            }
            BlockPos pos = base.offset(x, y, z);
            if (!fox.level().hasChunkAt(pos)
                    || WalkNodeEvaluator.getBlockPathTypeStatic(fox.level(), pos.mutable()) != BlockPathTypes.WALKABLE
                    || fox.level().getBlockState(pos.below()).getBlock() instanceof LeavesBlock
                    || !fox.level().noCollision(fox, fox.getBoundingBox().move(pos.subtract(fox.blockPosition())))) {
                continue;
            }
            fox.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, fox.getYRot(), fox.getXRot());
            fox.getNavigation().stop();
            break;
        }
    }
}
