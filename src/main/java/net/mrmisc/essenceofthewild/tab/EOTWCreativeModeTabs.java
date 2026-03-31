package net.mrmisc.essenceofthewild.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.item.EOTWItems;

import java.util.function.Supplier;

public class EOTWCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<CreativeModeTab> ITEM_TAB =
            createNewTab("item_tab", "Essence Of The Wild Items", ()-> EOTWItems.VANILLA_ICECREAM.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWItems.CONE.get());
                pOutput.accept(EOTWItems.VANILLA_ICECREAM.get());
                pOutput.accept(EOTWItems.STRAWBERRY_ICECREAM.get());
                pOutput.accept(EOTWItems.CHOCOLATE_ICECREAM.get());
                pOutput.accept(EOTWItems.FIRE_RESISTANCE_ICECREAM.get());
                pOutput.accept(EOTWItems.JUMP_BOOST_ICECREAM.get());
                pOutput.accept(EOTWItems.SPEED_ICECREAM.get());
            });

    public static RegistryObject<CreativeModeTab> SPAWN_EGG_TAB =
            createNewTab("spawn_egg_tab", "Essence Of The Wild Spawn Eggs", ()-> EOTWItems.SHEEP_SPAWN_EGG.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWItems.SHEEP_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.PIG_SPAWN_EGG.get());
            });

    public static RegistryObject<CreativeModeTab> createNewTab(
            String name,
            String title,
            Supplier<Item> icon,
            CreativeModeTab.DisplayItemsGenerator generator) {

        return TABS.register(name,
                () -> CreativeModeTab.builder()
                        .title(Component.literal(title))
                        .icon(() -> new ItemStack(icon.get()))
                        .displayItems(generator)
                        .build());
    }
}
