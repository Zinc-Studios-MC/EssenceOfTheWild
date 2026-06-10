package net.mrmisc.essenceofthewild.capability.custom.sleeping_bag;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class SleepingBagSpawn {
    private BlockPos originalPos;
    private BlockPos sbPos;

    public BlockPos getOriginalPos() {
        return originalPos;
    }

    public BlockPos getSbPos() {
        return sbPos;
    }

    public void setSbPos(BlockPos sbPos) {
        this.sbPos = sbPos;
    }

    public void setOriginalPos(BlockPos sleepingBagSpawnPos) {
        this.originalPos = sleepingBagSpawnPos;
    }

    public void saveNBTData(CompoundTag tag) {
        if (originalPos != null) {
            tag.putInt("X", originalPos.getX());
            tag.putInt("Y", originalPos.getY());
            tag.putInt("Z", originalPos.getZ());
        }
        if (sbPos != null) {
            tag.putInt("sX", sbPos.getX());
            tag.putInt("sY", sbPos.getY());
            tag.putInt("sZ", sbPos.getZ());
        }
    }

    public void loadNBTData(CompoundTag tag) {
        if (tag.contains("X")) {
            originalPos = new BlockPos(
                    tag.getInt("X"),
                    tag.getInt("Y"),
                    tag.getInt("Z")
            );
        }
        if (tag.contains("sX")) {
            sbPos = new BlockPos(
                    tag.getInt("sX"),
                    tag.getInt("sY"),
                    tag.getInt("sZ")
            );
        }
    }
}
