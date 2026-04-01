package net.mrmisc.essenceofthewild.entity.custom.chicken;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class ChickenVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/chicken.png"),
                    false, false);
    public static final MobVariant BASIC_GREY =
            new MobVariant("basic_brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/chicken_grey.png"),
                    false, false);
    public static final MobVariant COLD =
            new MobVariant("cold",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/cold_chicken.png"),
                    true, false);
    public static final MobVariant COLD_BROWN =
            new MobVariant("cold_brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/cold_chicken_brown.png"),
                    true, false);
    public static final MobVariant WARM =
            new MobVariant("warm",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/warm_chicken.png"),
                    false, true);
    public static final MobVariant WARM_BLACK =
            new MobVariant("warm",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/chicken/warm_chicken_black.png"),
                    false, true);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_GREY, WARM, COLD, WARM_BLACK, COLD_BROWN);
}
