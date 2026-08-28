package net.mrmisc.essenceofthewild.entity.custom;

import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenVariants;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowVariants;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckVariants;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretVariants;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareVariants;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigVariants;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitVariants;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepVariants;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VariantPickTest {
    private static final BooleanSupplier HEADS = () -> true;
    private static final BooleanSupplier TAILS = () -> false;

    private static BiomeQuery at(Habitat... habitats) {
        Set<Habitat> set = EnumSet.noneOf(Habitat.class);
        set.addAll(java.util.List.of(habitats));
        return set::contains;
    }

    @Test
    void coldAndHotBeatTheCoinFlip() {
        assertEquals(CowVariants.COLD, CowVariants.pick(at(Habitat.COLD), HEADS));
        assertEquals(CowVariants.COLD, CowVariants.pick(at(Habitat.COLD), TAILS));
        assertEquals(CowVariants.WARM, CowVariants.pick(at(Habitat.HOT), HEADS));
        assertEquals(SheepVariants.COLD, SheepVariants.pick(at(Habitat.COLD), TAILS));
        assertEquals(PigVariants.WARM, PigVariants.pick(at(Habitat.HOT), TAILS));
    }

    @Test
    void coldWinsWhenABiomeIsSomehowBothColdAndHot() {
        assertEquals(CowVariants.COLD, CowVariants.pick(at(Habitat.COLD, Habitat.HOT), HEADS));
        assertEquals(SheepVariants.COLD, SheepVariants.pick(at(Habitat.COLD, Habitat.HOT), HEADS));
        assertEquals(PigVariants.COLD, PigVariants.pick(at(Habitat.COLD, Habitat.HOT), HEADS));
    }

    @Test
    void plainBiomesFallBackToTheCoinFlip() {
        assertEquals(CowVariants.BASIC, CowVariants.pick(at(), HEADS));
        assertEquals(CowVariants.BASIC_BROWN, CowVariants.pick(at(), TAILS));
        assertEquals(SheepVariants.BASIC, SheepVariants.pick(at(), HEADS));
        assertEquals(SheepVariants.BASIC_GREY, SheepVariants.pick(at(), TAILS));
        assertEquals(RabbitVariants.BASIC, RabbitVariants.pick(at(), HEADS));
        assertEquals(RabbitVariants.BASIC_WHITE, RabbitVariants.pick(at(), TAILS));
    }

    @Test
    void rabbitOnlyHasAColdVariant() {
        assertEquals(RabbitVariants.COLD, RabbitVariants.pick(at(Habitat.COLD), HEADS));
        assertEquals(RabbitVariants.BASIC, RabbitVariants.pick(at(Habitat.HOT), HEADS));
    }

    @Test
    void hareIsSandyOnlyInTheDesert() {
        assertEquals(HareVariants.YELLOW, HareVariants.pick(at(Habitat.DESERT)));
        assertEquals(HareVariants.BROWN, HareVariants.pick(at()));
        assertEquals(HareVariants.BROWN, HareVariants.pick(at(Habitat.HOT)));
    }

    @Test
    void duckReadsWetBiomesBeforeFallingBack() {
        assertEquals(DuckVariants.BLUE, DuckVariants.pick(at(Habitat.MANGROVE_SWAMP), HEADS));
        assertEquals(DuckVariants.REDNECK, DuckVariants.pick(at(Habitat.MANGROVE_SWAMP), TAILS));
        assertEquals(DuckVariants.BROWN, DuckVariants.pick(at(Habitat.BAMBOO_JUNGLE), HEADS));
        assertEquals(DuckVariants.CREST, DuckVariants.pick(at(Habitat.SPARSE_JUNGLE), TAILS));
        assertEquals(DuckVariants.BASIC, DuckVariants.pick(at(Habitat.RIVER), HEADS));
        assertEquals(DuckVariants.GRAY, DuckVariants.pick(at(Habitat.SWAMP), TAILS));
        assertEquals(DuckVariants.BROWN, DuckVariants.pick(at(), TAILS));
    }

    @Test
    void mangroveSwampBeatsPlainSwampForDucks() {
        assertEquals(DuckVariants.BLUE, DuckVariants.pick(at(Habitat.MANGROVE_SWAMP, Habitat.SWAMP), HEADS));
    }

    @Test
    void ferretReadsNamedForestsBeforeTags() {
        assertEquals(FerretVariants.BASIC, FerretVariants.pick(at(Habitat.FLOWER_FOREST), TAILS));
        assertEquals(FerretVariants.RED, FerretVariants.pick(at(Habitat.OLD_GROWTH_PINE_TAIGA), TAILS));
        assertEquals(FerretVariants.WHITE, FerretVariants.pick(at(Habitat.SNOWY_TAIGA), TAILS));
        assertEquals(FerretVariants.BASIC, FerretVariants.pick(at(Habitat.PLAINS), TAILS));
        assertEquals(FerretVariants.RED, FerretVariants.pick(at(Habitat.HOT), TAILS));
        assertEquals(FerretVariants.WHITE, FerretVariants.pick(at(Habitat.COLD), TAILS));
    }

    @Test
    void aSnowyTaigaFerretIsWhiteNotJustCold() {
        assertEquals(FerretVariants.WHITE, FerretVariants.pick(at(Habitat.SNOWY_TAIGA, Habitat.COLD), HEADS));
        assertEquals(FerretVariants.RED, FerretVariants.pick(at(Habitat.OLD_GROWTH_PINE_TAIGA, Habitat.COLD), HEADS));
    }

    @Test
    void chickenPicksAPairPerClimate() {
        assertEquals(ChickenVariants.COLD, ChickenVariants.pick(at(Habitat.COLD), HEADS));
        assertEquals(ChickenVariants.COLD_BROWN, ChickenVariants.pick(at(Habitat.COLD), TAILS));
        assertEquals(ChickenVariants.WARM, ChickenVariants.pick(at(Habitat.HOT), HEADS));
        assertEquals(ChickenVariants.WARM_BLACK, ChickenVariants.pick(at(Habitat.HOT), TAILS));
        assertEquals(ChickenVariants.BASIC, ChickenVariants.pick(at(), HEADS));
    }

    @Test
    void everyPickerReturnsSomethingForAnUnremarkableBiome() {
        assertNotNull(CowVariants.pick(at(), HEADS));
        assertNotNull(SheepVariants.pick(at(), HEADS));
        assertNotNull(PigVariants.pick(at(), HEADS));
        assertNotNull(ChickenVariants.pick(at(), HEADS));
        assertNotNull(DuckVariants.pick(at(), HEADS));
        assertNotNull(FerretVariants.pick(at(), HEADS));
        assertNotNull(RabbitVariants.pick(at(), HEADS));
        assertNotNull(HareVariants.pick(at()));
    }
}
