package net.mrmisc.essenceofthewild.block.entity.custom.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;

// our version of HangingSignBlockEntity, extends SignBlockEntity instead because the vanilla hanging
// one only has a ctor that hardcodes BlockEntityType.HANGING_SIGN so theres no way to pass our own type,
// the two overrides below are just what the vanilla hanging sign adds on top
public class EOTWHangingSignBlockEntity extends SignBlockEntity {
    public EOTWHangingSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.MANGO_HANGING_SIGN.get(), pPos, pBlockState);
    }

    @Override
    public int getTextLineHeight() {
        return 9;
    }

    @Override
    public int getMaxTextLineWidth() {
        return 60;
    }
}
