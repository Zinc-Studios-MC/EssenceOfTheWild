package net.mrmisc.essenceofthewild.entity.custom.cow;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class CowVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/cow/cow.png"),
                    false, false);
    public static final MobVariant BASIC_BROWN =
            new MobVariant("basic_brown",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/cow/cow_brown.png"),
                    false, false);
    public static final MobVariant COLD =
            new MobVariant("cold",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/cow/cold_cow.png"),
                    true, false);
    public static final MobVariant WARM =
            new MobVariant("warm",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/cow/warm_cow.png"),
                    false, true);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_BROWN, WARM, COLD);
}
