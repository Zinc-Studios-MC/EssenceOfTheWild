package net.mrmisc.essenceofthewild.worldgen.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.HangingMangoDecorator;

public class EOTWTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<TreeDecoratorType<HangingMangoDecorator>> MANGO_DECORATOR = TREE_DECORATORS.register("mango",
            ()-> new TreeDecoratorType<>(HangingMangoDecorator.CODEC));
}
