package net.mrmisc.essenceofthewild.entity.custom.cow;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class CowVariants {
    public static final Variant BASIC = variant("basic", "cow.png", "baby_cow.png");
    public static final Variant BASIC_BROWN = variant("basic_brown", "cow_brown.png", "baby_cow_brown.png");
    public static final Variant COLD = variant("cold", "cold_cow.png", "baby_cold_cow.png");
    public static final Variant WARM = variant("warm", "warm_cow.png", "baby_warm_cow.png");
    public static final Variant COLD2 = variant("cold2", "cold_cow2.png", "baby_cold_cow2.png");
    public static final Variant WARM2 = variant("warm2", "warm_cow2.png", "baby_warm_cow2.png");
    public static final Variant WHITE = variant("white", "white_cow.png", "baby_white_cow.png");

    public static final List<Variant> ALL = List.of(BASIC, BASIC_BROWN, COLD, WARM, COLD2, WARM2, WHITE);

    private static Variant variant(String id, String adult, String baby) {
        return new Variant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/cow/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/cow/" + baby)
        );
    }

    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, ALL);

    public static Variant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.is(Habitat.COLD)) {
            return coin.getAsBoolean() ? COLD : COLD2;
        }
        if (at.is(Habitat.HOT)) {
            return coin.getAsBoolean() ? WARM : WARM2;
        }
        if (coin.getAsBoolean()) {
            return BASIC;
        }
        return coin.getAsBoolean() ? BASIC_BROWN : WHITE;
    }

}
