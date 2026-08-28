package net.mrmisc.essenceofthewild.entity.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VariantSetTest {
    private final VariantSet<String> set = VariantSet.of(Function.identity(), List.of("basic", "cold", "warm"));

    @Test
    void indexRoundTrips() {
        assertEquals(1, set.indexOf("cold"));
        assertEquals("cold", set.byIndex(1));
    }

    @Test
    void outOfRangeIndexFallsBackToFirst() {
        assertEquals("basic", set.byIndex(-1));
        assertEquals("basic", set.byIndex(3));
        assertEquals("basic", set.byIndex(Integer.MIN_VALUE));
    }

    @Test
    void unknownIdFallsBackToFirst() {
        assertEquals("basic", set.byId("does_not_exist"));
        assertEquals(0, set.indexOfId(""));
    }

    @Test
    void unknownVariantIndexesToFirstRatherThanMinusOne() {
        assertEquals(0, set.indexOf("not_in_the_set"));
    }

    @Test
    void emptySetIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> VariantSet.of(Function.identity(), List.of()));
    }
}
