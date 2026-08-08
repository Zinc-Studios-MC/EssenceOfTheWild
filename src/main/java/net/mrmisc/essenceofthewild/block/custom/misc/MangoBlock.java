package net.mrmisc.essenceofthewild.block.custom.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;

public class MangoBlock extends Block {
    private static final VoxelShape SHAPE = Shapes.or(
            // body
            Block.box(
                    4.0D, 3.0D, 4.0D,
                    12.0D, 13.0D, 12.0D
            ),

            // stem
            Block.box(
                    7.0D, 13.0D, 7.0D,
                    9.0D, 16.0D, 9.0D
            )
    );

    public MangoBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).is(EOTWBlocks.MANGO_LEAVES.get())
                || pLevel.getBlockState(pPos.above()).is(EOTWBlocks.VANILLA_LEAVES.get());
    }
}
