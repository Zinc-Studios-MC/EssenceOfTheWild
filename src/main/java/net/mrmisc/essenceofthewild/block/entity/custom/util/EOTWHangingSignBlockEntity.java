package net.mrmisc.essenceofthewild.block.entity.custom.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;

/**
 * Equivalent of vanilla HangingSignBlockEntity. It extends SignBlockEntity rather than
 * HangingSignBlockEntity because the latter only exposes a constructor that hardcodes
 * BlockEntityType.HANGING_SIGN, leaving no way to attach our own registered type. The two
 * overridden text metrics are what HangingSignBlockEntity itself adds.
 */
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
