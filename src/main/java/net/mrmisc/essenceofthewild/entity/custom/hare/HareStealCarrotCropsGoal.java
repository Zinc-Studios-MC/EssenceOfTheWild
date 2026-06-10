package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class HareStealCarrotCropsGoal extends RemoveBlockGoal {
    public HareStealCarrotCropsGoal(PathfinderMob pRemoverMob, double pSpeedModifier, int pSearchRange) {
        super(Blocks.CARROTS, pRemoverMob, pSpeedModifier, pSearchRange);
    }

    @Override
    public void playBreakSound(Level pLevel, BlockPos pPos) {
        pLevel.playSound(null, pPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + pLevel.random.nextFloat() * 0.2F);

    }

    @Override
    public double acceptedDistance() {
        return 1f;
    }
}
