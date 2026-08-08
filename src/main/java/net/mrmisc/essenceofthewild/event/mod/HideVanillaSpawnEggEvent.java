package net.mrmisc.essenceofthewild.event.mod;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

// hides the vanilla spawn eggs for mobs we fully replace so the creative menu isnt showing two eggs
// for the same animal, the vanilla egg still works if you get one another way since
// ReplaceVanillaMobEvent converts whatever it spawns, this just takes it out of the menu
@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HideVanillaSpawnEggEvent {

    // eggs whose mob we replace one for one, keep this matching ReplaceVanillaMobEvent or you end up
    // hiding an egg for a mob that never gets converted and theres no way to get that animal at all
    private static final Item[] REPLACED_SPAWN_EGGS = {
            Items.COW_SPAWN_EGG,
            Items.MOOSHROOM_SPAWN_EGG,
            Items.SHEEP_SPAWN_EGG,
            Items.PIG_SPAWN_EGG,
            Items.CHICKEN_SPAWN_EGG,
            Items.RABBIT_SPAWN_EGG
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
