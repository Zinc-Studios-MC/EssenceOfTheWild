package net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.server;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;

public class SleepingBagBlockEntity extends BlockEntity {
    public SleepingBagBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.SLEEPING_BAG.get(), pPos, pBlockState);
    }
}
