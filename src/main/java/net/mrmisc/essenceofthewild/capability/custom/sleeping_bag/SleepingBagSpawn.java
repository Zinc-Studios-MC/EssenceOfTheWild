package net.mrmisc.essenceofthewild.capability.custom.sleeping_bag;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class SleepingBagSpawn {
    private BlockPos sleepingBagSpawnPos;

    public BlockPos getSleepingBagSpawnPos() {
        return sleepingBagSpawnPos;
    }

    public void setOriginalPos(BlockPos sleepingBagSpawnPos) {
        this.sleepingBagSpawnPos = sleepingBagSpawnPos;
    }

    public void saveNBTData(CompoundTag tag) {
        if (sleepingBagSpawnPos != null) {
            tag.putInt("X", sleepingBagSpawnPos.getX());
            tag.putInt("Y", sleepingBagSpawnPos.getY());
            tag.putInt("Z", sleepingBagSpawnPos.getZ());
        }
    }

    public void loadNBTData(CompoundTag tag) {
        if (tag.contains("X")) {
            sleepingBagSpawnPos = new BlockPos(
                    tag.getInt("X"),
                    tag.getInt("Y"),
                    tag.getInt("Z")
            );
        }
    }
}
