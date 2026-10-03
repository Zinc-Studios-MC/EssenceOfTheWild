package net.mrmisc.essenceofthewild.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

public class EOTWSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EssenceOfTheWildMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DUCK_AMBIENT = register("entity.duck.ambient");
    public static final RegistryObject<SoundEvent> DUCK_HURT = register("entity.duck.hurt");
    public static final RegistryObject<SoundEvent> DUCK_DEATH = register("entity.duck.death");
    public static final RegistryObject<SoundEvent> RAT_AMBIENT = register("entity.rat.ambient");
    public static final RegistryObject<SoundEvent> RAT_HURT = register("entity.rat.hurt");
    public static final RegistryObject<SoundEvent> RAT_DEATH = register("entity.rat.death");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(EOTWUtils.getLoc(name)));
    }
}
