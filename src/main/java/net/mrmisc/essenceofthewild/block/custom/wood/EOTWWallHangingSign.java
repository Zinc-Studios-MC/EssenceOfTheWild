package net.mrmisc.essenceofthewild.block.custom.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.mrmisc.essenceofthewild.block.entity.custom.util.EOTWHangingSignBlockEntity;

public class EOTWWallHangingSign extends WallHangingSignBlock {
    public EOTWWallHangingSign(Properties pProperties, WoodType pType) {
        super(pProperties, pType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EOTWHangingSignBlockEntity(pPos, pState);
    }
}
