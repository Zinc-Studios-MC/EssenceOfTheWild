package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class SheepVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/sheep/sheep.png"),
                    false, false);
    public static final MobVariant BASIC_GREY =
            new MobVariant("basic_grey",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/sheep/sheep_grey.png"),
                    false, false);
    public static final MobVariant COLD =
            new MobVariant("cold",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/sheep/cold_sheep.png"),
                    true, false);
    public static final MobVariant WARM =
            new MobVariant("warm",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/sheep/warm_sheep.png"),
                    false, true);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_GREY, WARM, COLD);
}
