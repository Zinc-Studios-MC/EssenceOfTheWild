package net.mrmisc.essenceofthewild.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.item.EOTWItems;

import java.util.function.Supplier;

public class EOTWBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<Block> WOODEN_FREEZER = registerBlock("wooden_freezer",
            ()-> new WoodenFreezerBlock(BlockBehaviour.Properties.of().noOcclusion().noParticlesOnBreak()));

    public static RegistryObject<Block> CHEESE_MAKER = registerBlock("cheese_maker",
            ()-> new CheeseMakerBlock(BlockBehaviour.Properties.of().noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return EOTWItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
