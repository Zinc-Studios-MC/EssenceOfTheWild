package net.mrmisc.essenceofthewild.entity.custom.pig;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
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
    public static final MobVariant COLD2 =
            new MobVariant("cold2",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/cold_pig2.png"),
                    true, false);
    public static final MobVariant WARM2 =
            new MobVariant("warm2",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/pig/warm_pig2.png"),
                    false, true);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_GREY, WARM, COLD, COLD2, WARM2);

    public static final VariantSet<MobVariant> SET = VariantSet.of(MobVariant::id, ALL);

    public static MobVariant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.is(Habitat.COLD)) {
            return coin.getAsBoolean() ? COLD : COLD2;
        }
        if (at.is(Habitat.HOT)) {
            return coin.getAsBoolean() ? WARM : WARM2;
        }
        return coin.getAsBoolean() ? BASIC : BASIC_GREY;
    }

}
