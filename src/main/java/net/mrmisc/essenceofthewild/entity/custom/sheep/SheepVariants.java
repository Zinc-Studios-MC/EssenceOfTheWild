package net.mrmisc.essenceofthewild.entity.custom.sheep;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class SheepVariants {
    public static final Variant BASIC = variant("basic", "sheep.png", "baby_sheep.png");
    public static final Variant BASIC_GREY = variant("basic_grey", "sheep_grey.png", "baby_sheep_grey.png");
    public static final Variant COLD = variant("cold", "cold_sheep.png", "baby_cold_sheep.png");
    public static final Variant WARM = variant("warm", "warm_sheep.png", "baby_warm_sheep.png");

    public static final List<Variant> ALL = List.of(BASIC, BASIC_GREY, COLD, WARM);

    private static Variant variant(String id, String adult, String baby) {
        return new Variant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/" + baby)
        );
    }

    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, ALL);

    public static Variant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.is(Habitat.COLD)) {
            return COLD;
        }
        if (at.is(Habitat.HOT)) {
            return WARM;
        }
        return coin.getAsBoolean() ? BASIC : BASIC_GREY;
    }

}
