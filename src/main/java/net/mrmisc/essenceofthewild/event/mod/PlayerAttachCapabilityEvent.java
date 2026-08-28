package net.mrmisc.essenceofthewild.event.mod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.capability.custom.sleeping_bag.SleepingBagSpawn;
import net.mrmisc.essenceofthewild.capability.custom.sleeping_bag.SleepingBagSpawnProvider;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID)
public class PlayerAttachCapabilityEvent {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(SleepingBagSpawnProvider.SLEEPING_BAG_SPAWN).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "spawn"), new SleepingBagSpawnProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SleepingBagSpawn.class);
    }

}
