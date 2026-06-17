package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class DuckVariants {
    public static final DuckVariant BASIC = variant("basic", "duck_texture.png", "duckling_texture.png");
    public static final DuckVariant BLUE = variant("blue", "blue_duck_texture.png", "blue_duckling_texture.png");
    public static final DuckVariant BROWN = variant("brown", "brown_duck_texture.png", "brown_duckling_texture.png");
    public static final DuckVariant CREST = variant("crest", "crest_duck_texture.png", "crest_duckling_texture.png");
    public static final DuckVariant GRAY = variant("gray", "gray_duck_texture.png", "gray_duckling_texture.png");
    public static final DuckVariant REDNECK = variant("redneck", "redneck_duck_texture.png", "redneck_duckling_texture.png");

    public static final List<DuckVariant> ALL = List.of(BASIC, BLUE, BROWN, CREST, GRAY, REDNECK);

    private static DuckVariant variant(String id, String adult, String baby) {
        return new DuckVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/duck/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/duck/" + baby)
        );
    }
}
