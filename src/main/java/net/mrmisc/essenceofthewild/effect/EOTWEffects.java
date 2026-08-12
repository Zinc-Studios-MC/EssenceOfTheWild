package net.mrmisc.essenceofthewild.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.effect.custom.RabiesEffect;
import net.mrmisc.essenceofthewild.effect.custom.WebbedEffect;

public class EOTWEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<MobEffect> RABIES = EFFECTS.register("rabies",
            ()-> new RabiesEffect(MobEffectCategory.HARMFUL, 0xFFA500));

    public static RegistryObject<MobEffect> WEBBED = EFFECTS.register("webbed",
            ()-> new WebbedEffect(MobEffectCategory.HARMFUL, 0xE8E8F0));
}
