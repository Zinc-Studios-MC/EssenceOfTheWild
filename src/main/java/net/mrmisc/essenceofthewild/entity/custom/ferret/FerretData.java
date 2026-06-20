package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.nbt.CompoundTag;

public record FerretData(
    String ferretUUID,
    String variant,
    String ownerUUID,
    CompoundTag inventory,
    boolean isTamed,
    boolean isInLove
) {
    public FerretData {
        inventory = inventory == null ? new CompoundTag() : inventory.copy();
    }

    public FerretData(String ferretUUID, String variant, String ownerUUID, boolean isTamed, boolean isInLove) {
        this(ferretUUID, variant, ownerUUID, new CompoundTag(), isTamed, isInLove);
    }
}
