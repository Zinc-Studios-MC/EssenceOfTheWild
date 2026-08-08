package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class MooshroomVariants {
    public static final MooshroomVariant RED = variant("red", "mooshroom.png", "baby_mooshroom.png");
    public static final MooshroomVariant BROWN = variant("brown", "brown_mooshroom.png", "baby_brown_mooshroom.png");

    public static final List<MooshroomVariant> ALL = List.of(RED, BROWN);

    private static MooshroomVariant variant(String id, String adult, String baby) {
        return new MooshroomVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/mooshroom/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/mooshroom/" + baby)
        );
    }
}
