package net.mrmisc.essenceofthewild.block.entity.custom.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;

public class EOTWSignBlockEntity extends SignBlockEntity {
    public EOTWSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.MANGO_SIGN.get(), pPos, pBlockState);
    }
}
