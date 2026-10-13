package net.mrmisc.essenceofthewild.entity.custom.wolf;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

import java.util.ArrayList;
import java.util.List;

public class WolfVariants {
    public static final Variant WOLF = variant("wolf");
    public static final Variant COLD = variant("cold");
    public static final Variant SNOWY = variant("snowy");
    public static final Variant TAIGA = variant("taiga");
    public static final Variant BLACK = variant("black");
    public static final Variant FOREST = variant("forest");
    public static final Variant MOUNTAIN = variant("mountain");
    public static final Variant CHESTNUT = variant("chestnut");
    public static final Variant BROWN = variant("brown");
    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id,
            List.of(WOLF, COLD, SNOWY, TAIGA, BLACK, FOREST, MOUNTAIN, CHESTNUT, BROWN));

    private static Variant variant(String id) {
        String name = id.equals("wolf") ? "wolf" : id + "_wolf";
        return new Variant(id, EOTWUtils.getLoc("textures/entity/wolf/" + name + "_texture.png"),
                EOTWUtils.getLoc("textures/entity/wolf/baby_" + name + "_texture.png"));
    }

    public static List<Variant> at(Holder<Biome> biome) {
        var choices = new ArrayList<Variant>();
        boolean snowMountain = biome.is(Biomes.SNOWY_SLOPES) || biome.is(Biomes.FROZEN_PEAKS) || biome.is(Biomes.JAGGED_PEAKS);
        if (biome.is(Biomes.SNOWY_PLAINS) || snowMountain) {
            choices.add(COLD);
        }
        if (biome.is(Biomes.SNOWY_TAIGA) || snowMountain) {
            choices.add(SNOWY);
        }
        if (biome.is(Biomes.SNOWY_TAIGA) || biome.is(Biomes.TAIGA)) {
            choices.add(TAIGA);
        }
        if (biome.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || biome.is(Biomes.OLD_GROWTH_PINE_TAIGA)) {
            choices.add(BLACK);
            choices.add(FOREST);
        }
        if (biome.is(Biomes.WINDSWEPT_HILLS) || biome.is(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                || biome.is(Biomes.WINDSWEPT_FOREST) || biome.is(Biomes.MEADOW)) {
            choices.add(MOUNTAIN);
        }
        if (biome.is(Biomes.TAIGA) || biome.is(Biomes.CHERRY_GROVE)) {
            choices.add(CHESTNUT);
        }
        if (biome.is(Biomes.FOREST) || biome.is(Biomes.BIRCH_FOREST) || biome.is(Biomes.OLD_GROWTH_BIRCH_FOREST)) {
            choices.add(BROWN);
        }
        if (biome.is(Biomes.FOREST) || biome.is(Biomes.FLOWER_FOREST) || biome.is(Biomes.DARK_FOREST)
                || biome.is(Biomes.BIRCH_FOREST) || biome.is(Biomes.OLD_GROWTH_BIRCH_FOREST) || biome.is(Biomes.WINDSWEPT_FOREST)) {
            choices.add(WOLF);
        }
        return choices;
    }

    public static Variant pick(Holder<Biome> biome, RandomSource random) {
        var choices = at(biome);
        return choices.isEmpty() ? WOLF : choices.get(random.nextInt(choices.size()));
    }

    public static boolean bigCollar(Variant variant) {
        return variant == COLD || variant == SNOWY || variant == TAIGA;
    }
}
