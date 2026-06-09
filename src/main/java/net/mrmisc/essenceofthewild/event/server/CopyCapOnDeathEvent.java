package net.mrmisc.essenceofthewild.event.server;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.capability.custom.sleeping_bag.SleepingBagSpawnProvider;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CopyCapOnDeathEvent {
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(SleepingBagSpawnProvider.SLEEPING_BAG_SPAWN).ifPresent(oldCap ->
                event.getEntity().getCapability(SleepingBagSpawnProvider.SLEEPING_BAG_SPAWN).ifPresent(newCap ->
                        newCap.setOriginalPos(oldCap.getSleepingBagSpawnPos())));
        event.getOriginal().invalidateCaps();
    }
}
