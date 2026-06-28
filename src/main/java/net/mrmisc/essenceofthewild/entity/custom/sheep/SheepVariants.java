package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class SheepVariants {
    public static final SheepVariant BASIC = variant("basic", "sheep.png", "baby_sheep.png");
    public static final SheepVariant BASIC_GREY = variant("basic_grey", "sheep_grey.png", "baby_sheep_grey.png");
    public static final SheepVariant COLD = variant("cold", "cold_sheep.png", "baby_cold_sheep.png");
    public static final SheepVariant WARM = variant("warm", "warm_sheep.png", "baby_warm_sheep.png");

    public static final List<SheepVariant> ALL = List.of(BASIC, BASIC_GREY, COLD, WARM);

    private static SheepVariant variant(String id, String adult, String baby) {
        return new SheepVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/" + baby)
        );
    }
}
