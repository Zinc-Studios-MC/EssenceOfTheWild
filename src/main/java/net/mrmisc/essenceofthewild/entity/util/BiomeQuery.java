package net.mrmisc.essenceofthewild.entity.util;

public interface BiomeQuery {
    boolean is(Habitat habitat);

    default boolean isAny(Habitat first, Habitat... rest) {
        if (is(first)) {
            return true;
        }
        for (Habitat h : rest) {
            if (is(h)) {
                return true;
            }
        }
        return false;
    }
}
