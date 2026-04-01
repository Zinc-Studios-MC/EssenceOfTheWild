package net.mrmisc.essenceofthewild.entity.custom.pig;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class PigVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/pig.png"),
                    false, false);
    public static final MobVariant BASIC_GREY =
            new MobVariant("basic_grey",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/pig_grey.png"),
                    false, false);
    public static final MobVariant COLD =
            new MobVariant("cold",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/cold_pig.png"),
                    true, false);
    public static final MobVariant WARM =
            new MobVariant("warm",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/warm_pig.png"),
                    false, true);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_GREY, WARM, COLD);
}
