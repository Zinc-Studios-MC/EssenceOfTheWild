package net.mrmisc.essenceofthewild.event.mod;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HideVanillaSpawnEggEvent {

    private static final Item[] REPLACED_SPAWN_EGGS = {
            Items.COW_SPAWN_EGG,
            Items.MOOSHROOM_SPAWN_EGG,
            Items.SHEEP_SPAWN_EGG,
            Items.PIG_SPAWN_EGG,
            Items.CHICKEN_SPAWN_EGG,
            Items.RABBIT_SPAWN_EGG,
            Items.SPIDER_SPAWN_EGG,
            Items.CAVE_SPIDER_SPAWN_EGG
    };

    @SubscribeEvent
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.SPAWN_EGGS) {
            return;
        }
        for (Item egg : REPLACED_SPAWN_EGGS) {
            event.getEntries().remove(new ItemStack(egg));
        }
    }
}
