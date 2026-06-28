package net.mrmisc.essenceofthewild.entity.custom.cow;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class CowVariants {
    public static final CowVariant BASIC = variant("basic", "cow.png", "baby_cow.png");
    public static final CowVariant BASIC_BROWN = variant("basic_brown", "cow_brown.png", "baby_cow_brown.png");
    public static final CowVariant COLD = variant("cold", "cold_cow.png", "baby_cold_cow.png");
    public static final CowVariant WARM = variant("warm", "warm_cow.png", "baby_warm_cow.png");

    public static final List<CowVariant> ALL = List.of(BASIC, BASIC_BROWN, COLD, WARM);

    private static CowVariant variant(String id, String adult, String baby) {
        return new CowVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/cow/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/cow/" + baby)
        );
    }
}