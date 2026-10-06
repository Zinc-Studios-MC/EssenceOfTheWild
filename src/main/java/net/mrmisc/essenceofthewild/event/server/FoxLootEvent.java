package net.mrmisc.essenceofthewild.event.server;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.fox.FoxEntity;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FoxLootEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void drops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof FoxEntity fox) || !fox.readyToHunt() || !fox.isPouncing()) {
            return;
        }
        var drops = event.getDrops().stream().filter(drop -> !drop.getItem().isEmpty()).toList();
        if (drops.isEmpty()) {
            return;
        }
        ItemStack stack = drops.get(fox.getRandom().nextInt(drops.size())).getItem().copy();
        int count = 0;
        for (ItemEntity drop : drops) {
            ItemStack items = drop.getItem();
            if (ItemStack.isSameItemSameTags(stack, items)) {
                int take = Math.min(items.getCount(), stack.getMaxStackSize() - count);
                items.shrink(take);
                count += take;
                if (items.isEmpty()) {
                    event.getDrops().remove(drop);
                }
            }
        }
        stack.setCount(count);
        fox.fetch(stack);
    }
}
