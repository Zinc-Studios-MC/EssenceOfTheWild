package net.mrmisc.essenceofthewild.entity.util;

import java.util.List;
import java.util.function.Function;

public final class VariantSet<V> {
    private final List<V> all;
    private final Function<V, String> idOf;

    public static <V> VariantSet<V> of(Function<V, String> idOf, List<V> all) {
        if (all.isEmpty()) {
            throw new IllegalArgumentException("variant set is empty");
        }
        return new VariantSet<>(idOf, all);
    }

    private VariantSet(Function<V, String> idOf, List<V> all) {
        this.idOf = idOf;
        this.all = List.copyOf(all);
    }

    public List<V> all() {
        return all;
    }

    public V first() {
        return all.get(0);
    }

    public V byIndex(int i) {
        return i >= 0 && i < all.size() ? all.get(i) : first();
    }

    public int indexOf(V v) {
        int i = all.indexOf(v);
        return i < 0 ? 0 : i;
    }

    public int indexOfId(String id) {
        for (int i = 0; i < all.size(); i++) {
            if (idOf.apply(all.get(i)).equals(id)) {
                return i;
            }
        }
        return 0;
    }

    public V byId(String id) {
        return all.get(indexOfId(id));
    }

    public String idOf(V v) {
        return idOf.apply(v);
    }
}
