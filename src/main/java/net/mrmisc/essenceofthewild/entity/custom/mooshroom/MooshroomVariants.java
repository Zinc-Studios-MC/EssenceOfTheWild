package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import java.util.List;

public class MooshroomVariants {
    public static final Variant RED = variant("red", "mooshroom.png", "baby_mooshroom.png");
    public static final Variant BROWN = variant("brown", "brown_mooshroom.png", "baby_brown_mooshroom.png");

    public static final List<Variant> ALL = List.of(RED, BROWN);

    private static Variant variant(String id, String adult, String baby) {
        return new Variant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/mooshroom/" + adult),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/mooshroom/" + baby)
        );
    }

    public static final VariantSet<Variant> SET = VariantSet.of(Variant::id, ALL);

}
