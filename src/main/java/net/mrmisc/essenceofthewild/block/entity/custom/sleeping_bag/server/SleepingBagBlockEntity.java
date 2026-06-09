package net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;

public class SleepingBagBlockEntity extends BlockEntity {
    private DyeColor color;
    public SleepingBagBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EOTWBlockEntities.SLEEPING_BAG.get(), pPos, pBlockState);

    }
    public SleepingBagBlockEntity(DyeColor color, BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState);
        setColor(color);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor pColor) {
        this.color = pColor;
    }
}
