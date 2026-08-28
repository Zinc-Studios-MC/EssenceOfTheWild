package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class HareVariants {
    public static final MobVariant YELLOW =
            new MobVariant("yellow",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/hare/hare.png"),
                    false, true);
    public static final MobVariant BROWN =
            new MobVariant("brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/hare/brown_hare.png"),
                    false, false);

    public static final List<MobVariant> ALL = List.of(YELLOW, BROWN);

    public static final VariantSet<MobVariant> SET = VariantSet.of(MobVariant::id, ALL);

    public static MobVariant pick(BiomeQuery at) {
        return at.is(Habitat.DESERT) ? YELLOW : BROWN;
    }

}
