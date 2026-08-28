package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class FerretVariants {
    public static final Variant BASIC = variant("basic", "ferret_texture.png", "baby_ferret_texture.png");
    public static final Variant RED = variant("red", "red_ferret_texture.png", "baby_red_ferret_texture.png");
    public static final Variant WHITE = variant("white", "white_ferret_texture.png", "baby_white_ferret_texture.png");

    public static final List<Variant> ALL = List.of(BASIC, RED, WHITE);

    private static Variant variant(String id, String adult, String baby) {
        return new Variant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/ferret/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/ferret/" + baby)
        );
    }

    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, ALL);

    public static Variant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.isAny(Habitat.FOREST, Habitat.BIRCH_FOREST, Habitat.FLOWER_FOREST)) {
            return BASIC;
        }
        if (at.isAny(Habitat.OLD_GROWTH_SPRUCE_TAIGA, Habitat.OLD_GROWTH_PINE_TAIGA)) {
            return RED;
        }
        if (at.isAny(Habitat.TAIGA, Habitat.SNOWY_TAIGA)) {
            return WHITE;
        }
        if (at.is(Habitat.PLAINS)) {
            return BASIC;
        }
        if (at.is(Habitat.HOT)) {
            return RED;
        }
        if (at.is(Habitat.COLD)) {
            return WHITE;
        }
        return coin.getAsBoolean() ? BASIC : RED;
    }

}
