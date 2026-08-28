package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;

public final class VariantSlot<V> {
    private static final String TAG = "Variant";

    private final SynchedEntityData data;
    private final EntityDataAccessor<Integer> key;
    private final VariantSet<V> set;

    public VariantSlot(SynchedEntityData data, EntityDataAccessor<Integer> key, VariantSet<V> set) {
        this.data = data;
        this.key = key;
        this.set = set;
    }

    public V get() {
        return set.byIndex(data.get(key));
    }

    public void set(V v) {
        data.set(key, set.indexOf(v));
    }

    public String id() {
        return set.idOf(get());
    }

    public void setById(String id) {
        data.set(key, set.indexOfId(id));
    }

    public void save(CompoundTag tag) {
        tag.putString(TAG, id());
    }

    public void load(CompoundTag tag) {
        setById(tag.getString(TAG));
    }

    public boolean isStoredIn(CompoundTag tag) {
        return tag.contains(TAG);
    }
}
