package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

public class FerretVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/ferret/ferret_texture.png"),
                    false, false);
    public static final MobVariant RED_FERRET =
            new MobVariant("red_ferret",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/ferret/red_ferret_texture.png"),
                    false, false);
    public static final MobVariant WHITE_FERRET =
            new MobVariant("white_ferret",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/ferret/white_ferret_texture.png"),
                    false, false);

    public static final List<MobVariant> ALL = List.of(BASIC, RED_FERRET, WHITE_FERRET);
}
