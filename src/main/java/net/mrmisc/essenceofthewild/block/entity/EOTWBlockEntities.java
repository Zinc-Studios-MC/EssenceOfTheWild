package net.mrmisc.essenceofthewild.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker.CheeseMakerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.util.EOTWSignBlockEntity;

import java.util.Set;

public class EOTWBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<BlockEntityType<WoodenFreezerBlockEntity>> WOODEN_FREEZER = BLOCK_ENTITY.register("wooden_freezer",
            ()-> new BlockEntityType<>(WoodenFreezerBlockEntity::new, Set.of(EOTWBlocks.WOODEN_FREEZER.get()), null));

    public static final RegistryObject<BlockEntityType<EOTWSignBlockEntity>> MOD_SIGN =
            BLOCK_ENTITY.register("mod_sign", () ->
                    BlockEntityType.Builder.of(EOTWSignBlockEntity::new,
                            EOTWBlocks.MANGO_SIGN.get(), EOTWBlocks.MANGO_WALL_SIGN.get(),
                            EOTWBlocks.MANGO_HANGING_SIGN.get(), EOTWBlocks.MANGO_WALL_HANGING_SIGN.get()).build(null));

    public static RegistryObject<BlockEntityType<CheeseMakerBlockEntity>> CHEESE_MAKER = BLOCK_ENTITY.register("cheese_maker",
            ()-> new BlockEntityType<>(CheeseMakerBlockEntity::new, Set.of(EOTWBlocks.CHEESE_MAKER.get()), null));

    public static RegistryObject<BlockEntityType<NestBlockEntity>> NEST = BLOCK_ENTITY.register("nest",
            ()-> new BlockEntityType<>(NestBlockEntity::new, Set.of(EOTWBlocks.NEST.get()), null));
}
