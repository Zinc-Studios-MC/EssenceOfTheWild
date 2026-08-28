package net.mrmisc.essenceofthewild.entity.custom.duck;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class DuckVariants {
    public static final Variant BASIC = variant("basic", "duck_texture.png", "duckling_texture.png");
    public static final Variant BLUE = variant("blue", "blue_duck_texture.png", "blue_duckling_texture.png");
    public static final Variant BROWN = variant("brown", "brown_duck_texture.png", "brown_duckling_texture.png");
    public static final Variant CREST = variant("crest", "crest_duck_texture.png", "crest_duckling_texture.png");
    public static final Variant GRAY = variant("gray", "gray_duck_texture.png", "gray_duckling_texture.png");
    public static final Variant REDNECK = variant("redneck", "redneck_duck_texture.png", "redneck_duckling_texture.png");

    public static final List<Variant> ALL = List.of(BASIC, BLUE, BROWN, CREST, GRAY, REDNECK);

    private static Variant variant(String id, String adult, String baby) {
        return new Variant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/duck/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/duck/" + baby)
        );
    }

    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, ALL);

    public static Variant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.is(Habitat.MANGROVE_SWAMP)) {
            return coin.getAsBoolean() ? BLUE : REDNECK;
        }
        if (at.isAny(Habitat.JUNGLE, Habitat.BAMBOO_JUNGLE, Habitat.SPARSE_JUNGLE)) {
            return coin.getAsBoolean() ? BROWN : CREST;
        }
        if (at.isAny(Habitat.RIVER, Habitat.SWAMP)) {
            return coin.getAsBoolean() ? BASIC : GRAY;
        }
        return coin.getAsBoolean() ? BASIC : BROWN;
    }

}
