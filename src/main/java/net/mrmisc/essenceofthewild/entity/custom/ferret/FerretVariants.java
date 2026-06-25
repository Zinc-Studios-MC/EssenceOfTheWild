package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class FerretVariants {
public static final FerretVariant BASIC = variant("basic", "ferret_texture.png", "baby_ferret_texture.png");
    public static final FerretVariant RED = variant("red", "red_ferret_texture.png", "baby_red_ferret_texture.png");
    public static final FerretVariant WHITE = variant("white", "white_ferret_texture.png", "baby_white_ferret_texture.png");

    public static final List<FerretVariant> ALL = List.of(BASIC, RED, WHITE);

    private static FerretVariant variant(String id, String adult, String baby) {
        return new FerretVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/ferret/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/ferret/" + baby)
        );
    }
}
