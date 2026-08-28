package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class DigGoal extends Goal {
    private static final int DIG_DURATION = 30;

    private final FerretEntity ferret;
    private int diggingTicks;

    public DigGoal(FerretEntity ferret) {
        this.ferret = ferret;
    }

    @Override
    public boolean canUse() {
        return !ferret.isBaby() && ferret.shouldDig() && !isUnset(ferret.getBlockToDig());
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void stop() {
        this.diggingTicks = 0;
        ferret.setDiggingIn(false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        BlockPos pos = ferret.getBlockToDig();
        BlockState state = ferret.level().getBlockState(pos);
        if (!ferret.getInventory().hasAnyOf(Set.of(Items.GOLDEN_CARROT)) && !isSuspicious(state)) {
            ferret.setDiggingIn(false);
            ferret.setShouldDig(false);
            diggingTicks = 0;
            return;
        }

        AABB bound = ferret.getBoundingBox().inflate(1);
        if (!bound.contains(Vec3.atCenterOf(pos))) {
            ferret.setDiggingIn(false);
            diggingTicks = 0;
            ferret.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1);
            return;
        }

        if (diggingTicks <= 0) {
            ferret.setDiggingIn(true);
            diggingTicks = DIG_DURATION;
        } else if (diggingTicks == 1) {
            finish(pos, state);
            --diggingTicks;
        } else {
            --diggingTicks;
        }
    }

    private void finish(BlockPos pos, BlockState state) {
        if (isSuspicious(state)) {
            Player owner = ferret.getOwner() instanceof Player player ? player : null;
            ItemStack found = BuriedLoot.rollAt((ServerLevel) ferret.level(), pos,
                    Vec3.atCenterOf(ferret.getOnPos().above()), owner);
            ferret.level().destroyBlock(pos, false);
            if (state.is(Blocks.SUSPICIOUS_SAND)) {
                ferret.level().setBlock(pos, Blocks.SAND.defaultBlockState(), 2);
            } else if (state.is(Blocks.SUSPICIOUS_GRAVEL)) {
                ferret.level().setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 2);
            }
            if (!found.isEmpty()) {
                store(found);
            }
        } else {
            ferret.level().destroyBlock(pos, true);
        }
        ferret.setDiggingIn(false);
        ferret.setShouldDig(false);
    }

    private void store(ItemStack found) {
        for (int i = 0; i < ferret.getInventory().getContainerSize(); i++) {
            ItemStack it = ferret.getInventory().getItem(i);
            if (it.is(Items.AIR)) {
                ferret.getInventory().setItem(i, found);
                break;
            } else if (it.is(found.getItem())) {
                it.setCount(it.getCount() + found.getCount());
                break;
            }
        }
        if (!ferret.getInventory().hasAnyOf(Set.of(found.getItem()))) {
            ItemEntity dropped = new ItemEntity(ferret.level(), ferret.getX(), ferret.getY() + 1, ferret.getZ(), found);
            ferret.level().addFreshEntity(dropped);
        }
    }

    private static boolean isSuspicious(BlockState state) {
        return state.is(Blocks.SUSPICIOUS_SAND) || state.is(Blocks.SUSPICIOUS_GRAVEL);
    }

    private static boolean isUnset(BlockPos pos) {
        return pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0;
    }
}
