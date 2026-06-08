package net.mrmisc.essenceofthewild.block.custom.sleeping_bag;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SleepingBagPart implements StringRepresentable {
    HEAD("head"),
    FOOT("foot");

    private final String name;

    SleepingBagPart(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }
}
