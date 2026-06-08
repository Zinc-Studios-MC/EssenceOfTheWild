package net.mrmisc.essenceofthewild.event.server.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.sleeping_bag.SleepingBagBlock;
import net.mrmisc.essenceofthewild.capability.custom.sleeping_bag.SleepingBagSpawnProvider;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SleepingBagSetSpawnEvent {
    @SubscribeEvent
    public static void onSpawnSet(PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();
        if(pos == null) return;
        if(event.getEntity().level().isClientSide()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();

        BlockState state = player.serverLevel().getBlockState(pos);

        if(state.getBlock() instanceof BedBlock && !(state.getBlock() instanceof SleepingBagBlock)) {
            player.getCapability(SleepingBagSpawnProvider.SLEEPING_BAG_SPAWN).ifPresent(data -> data.setOriginalPos(pos));
        }
    }
}
