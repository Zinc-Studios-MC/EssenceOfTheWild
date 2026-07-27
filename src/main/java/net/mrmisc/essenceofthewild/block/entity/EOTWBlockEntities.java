package net.mrmisc.essenceofthewild.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.burrow.BurrowBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker.CheeseMakerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.server.SleepingBagBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.util.EOTWHangingSignBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.util.EOTWSignBlockEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class EOTWBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<BlockEntityType<WoodenFreezerBlockEntity>> WOODEN_FREEZER = BLOCK_ENTITY.register("wooden_freezer",
            ()-> new BlockEntityType<>(WoodenFreezerBlockEntity::new, Set.of(EOTWBlocks.WOODEN_FREEZER.get()), null));
    public static RegistryObject<BlockEntityType<SleepingBagBlockEntity>> SLEEPING_BAG = BLOCK_ENTITY.register("sleeping_bag",
            () -> new BlockEntityType<>(SleepingBagBlockEntity::new,
                    EOTWBlocks.getSleepingBags().stream().map(RegistryObject::get).collect(Collectors.toSet()),null));

    public static RegistryObject<BlockEntityType<BurrowBlockEntity>> BURROW_BLOCK_ENTITY = BLOCK_ENTITY.register("burrow_block_entity",
            ()-> new BlockEntityType<>(BurrowBlockEntity::new, Set.of(EOTWBlocks.DIRT_BURROW_BLOCK.get(), EOTWBlocks.SAND_BURROW_BLOCK.get(), EOTWBlocks.MUD_BURROW_BLOCK.get()), null));


    // Split the way vanilla splits BlockEntityType.SIGN from BlockEntityType.HANGING_SIGN: the two
    // need different renderers, so they cannot share one type.
    public static final RegistryObject<BlockEntityType<EOTWSignBlockEntity>> MANGO_SIGN =
            BLOCK_ENTITY.register("mango_sign", () ->
                    BlockEntityType.Builder.of(EOTWSignBlockEntity::new,
                            EOTWBlocks.MANGO_SIGN.get(), EOTWBlocks.MANGO_WALL_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<EOTWHangingSignBlockEntity>> MANGO_HANGING_SIGN =
            BLOCK_ENTITY.register("mango_hanging_sign", () ->
                    BlockEntityType.Builder.of(EOTWHangingSignBlockEntity::new,
                            EOTWBlocks.MANGO_HANGING_SIGN.get(), EOTWBlocks.MANGO_WALL_HANGING_SIGN.get()).build(null));

    public static RegistryObject<BlockEntityType<CheeseMakerBlockEntity>> CHEESE_MAKER = BLOCK_ENTITY.register("cheese_maker",
            ()-> new BlockEntityType<>(CheeseMakerBlockEntity::new, Set.of(EOTWBlocks.CHEESE_MAKER.get()), null));

    public static RegistryObject<BlockEntityType<NestBlockEntity>> NEST = BLOCK_ENTITY.register("nest",
            ()-> new BlockEntityType<>(NestBlockEntity::new, Set.of(EOTWBlocks.NEST.get()), null));
}
