package net.mrmisc.essenceofthewild.capability.custom.sleeping_bag;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SleepingBagSpawnProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<SleepingBagSpawn> SLEEPING_BAG_SPAWN = CapabilityManager.get(new CapabilityToken<>() {});

    private SleepingBagSpawn sleepingBagSpawn = null;
    private final LazyOptional<SleepingBagSpawn> optional = LazyOptional.of(this::createSleepingBagSpawn);

    private SleepingBagSpawn createSleepingBagSpawn() {
        if(this.sleepingBagSpawn == null) {
            this.sleepingBagSpawn = new SleepingBagSpawn();
        }

        return this.sleepingBagSpawn;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return capability == SLEEPING_BAG_SPAWN ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createSleepingBagSpawn().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createSleepingBagSpawn().loadNBTData(tag);
    }
}
