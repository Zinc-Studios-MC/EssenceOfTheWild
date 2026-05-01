package net.mrmisc.essenceofthewild.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.item.EOTWItems;

import java.util.function.Supplier;

public class EOTWCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<CreativeModeTab> SPAWN_EGG_TAB =
            createNewTabWithItemIcon("spawn_egg_tab", "Essence Of The Wild Spawn Eggs", ()-> EOTWItems.SHEEP_SPAWN_EGG.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWItems.SHEEP_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.PIG_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.COW_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.MOOSHROOM_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.CHICKEN_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.RABBIT_SPAWN_EGG.get());
                pOutput.accept(EOTWItems.HARE_SPAWN_EGG.get());
            });

    public static RegistryObject<CreativeModeTab> FOOD_TAB =
            createNewTabWithItemIcon("food_tab", "Essence Of The Wild Food", ()-> EOTWItems.VANILLA_ICECREAM.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWItems.CONE.get());
                pOutput.accept(EOTWItems.VANILLA_ICECREAM.get());
                pOutput.accept(EOTWItems.CHOCOLATE_ICECREAM.get());
                pOutput.accept(EOTWItems.STRAWBERRY_ICECREAM.get());
                pOutput.accept(EOTWItems.FIRE_RESISTANCE_ICECREAM.get());
                pOutput.accept(EOTWItems.SPEED_ICECREAM.get());
                pOutput.accept(EOTWItems.JUMP_BOOST_ICECREAM.get());
                pOutput.accept(EOTWItems.SHEEP_MILK_BUCKET.get());
                pOutput.accept(EOTWItems.SHEEP_CHEESE.get());
                pOutput.accept(EOTWItems.SHEEP_CHEESE_WEDGE.get());
                pOutput.accept(EOTWBlocks.MANGO.get());
            });
    public static RegistryObject<CreativeModeTab> NATURAL_TAB =
            createNewTabWithBlockIcon("natural_tab", "Essence Of The Wild Natural Stuff", ()-> EOTWBlocks.MANGO_LOG.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWBlocks.MANGO_SAPLING.get());
                pOutput.accept(EOTWBlocks.MANGO_LOG.get());
                pOutput.accept(EOTWBlocks.STRIPPED_MANGO_LOG.get());
                pOutput.accept(EOTWBlocks.MANGO_LEAVES.get());
                pOutput.accept(EOTWBlocks.VANILLA_LEAVES.get());
                pOutput.accept(EOTWItems.VANILLA_FLOWER.get());
                pOutput.accept(EOTWItems.VANILLA_STICK.get());
            });
    public static RegistryObject<CreativeModeTab> MANMADE_TAB =
            createNewTabWithBlockIcon("manmade_tab", "Essence Of The Wild Blocks And Items", ()-> EOTWBlocks.WOODEN_FREEZER.get(), (pParameters, pOutput) -> {
                pOutput.accept(EOTWBlocks.MANGO_WOOD.get());
                pOutput.accept(EOTWBlocks.STRIPPED_MANGO_WOOD.get());
                pOutput.accept(EOTWBlocks.WOODEN_FREEZER.get());
                pOutput.accept(EOTWBlocks.CHEESE_MAKER.get());
            });


    public static RegistryObject<CreativeModeTab> createNewTabWithItemIcon(
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

    public static RegistryObject<CreativeModeTab> createNewTabWithBlockIcon(
            String name,
            String title,
            Supplier<Block> icon,
            CreativeModeTab.DisplayItemsGenerator generator) {

        return TABS.register(name,
                () -> CreativeModeTab.builder()
                        .title(Component.literal(title))
                        .icon(() -> new ItemStack(icon.get()))
                        .displayItems(generator)
                        .build());
    }
}
