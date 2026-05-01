package net.mrmisc.essenceofthewild.block.custom.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;

public class MangoBlock extends Block {
    public MangoBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).is(EOTWBlocks.MANGO_LEAVES.get()) || pLevel.getBlockState(pPos.above()).is(EOTWBlocks.VANILLA_LEAVES.get());
    }
}
