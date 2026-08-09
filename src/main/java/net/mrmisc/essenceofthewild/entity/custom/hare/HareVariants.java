package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class HareVariants {
    // sandy coat, this is the desert one
    public static final MobVariant YELLOW =
            new MobVariant("yellow",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/hare/hare.png"),
                    false, true);
    // savanna and plains
    public static final MobVariant BROWN =
            new MobVariant("brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/hare/brown_hare.png"),
                    false, false);

    public static final List<MobVariant> ALL = List.of(YELLOW, BROWN);
}
