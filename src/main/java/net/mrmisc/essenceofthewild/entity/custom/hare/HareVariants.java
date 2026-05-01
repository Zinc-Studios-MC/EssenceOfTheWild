package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class HareVariants {public static final MobVariant BASIC =
        new MobVariant("red",
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                        "textures/entity/hare/hare.png"),
                false, false);
    public static final MobVariant BROWN =
            new MobVariant("brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/hare/brown_hare.png"),
                    false, false);

    public static final List<MobVariant> ALL = List.of(BASIC, BROWN);
}
