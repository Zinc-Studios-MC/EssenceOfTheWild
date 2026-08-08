package net.mrmisc.essenceofthewild.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.custom.DuckEggItem;
import net.mrmisc.essenceofthewild.item.custom.icecream.EffectIceCream;
import net.mrmisc.essenceofthewild.item.custom.icecream.IceCream;
import net.mrmisc.essenceofthewild.item.custom.tools.IceAxe;
import net.mrmisc.essenceofthewild.item.custom.tools.UnderwaterArrowItem;

public class EOTWItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EssenceOfTheWildMod.MOD_ID);

    //icecream
    public static RegistryObject<Item> VANILLA_ICECREAM = createIceCream("vanilla");
    public static RegistryObject<Item> CHOCOLATE_ICECREAM = createIceCream("chocolate");
    public static RegistryObject<Item> STRAWBERRY_ICECREAM = createIceCream("strawberry");
    public static RegistryObject<Item> FIRE_RESISTANCE_ICECREAM = createEffectIceCream("fire_resistance", MobEffects.FIRE_RESISTANCE);
    public static RegistryObject<Item> JUMP_BOOST_ICECREAM = createEffectIceCream("jump_boost", MobEffects.JUMP);
    public static RegistryObject<Item> SPEED_ICECREAM = createEffectIceCream("speed", MobEffects.MOVEMENT_SPEED);

    //items
    public static RegistryObject<Item> CONE = ITEMS.register("cone", ()-> new Item(new Item.Properties().stacksTo(8)));
    public static RegistryObject<Item> SHEEP_CHEESE = ITEMS.register("sheep_cheese", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> SHEEP_CHEESE_WEDGE = ITEMS.register("sheep_cheese_wedge", ()-> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f).build())));
    public static RegistryObject<Item> ICE_CUBES = ITEMS.register("ice_cubes", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> ICE_AXE = ITEMS.register("ice_axe", ()-> new IceAxe(new Item.Properties().durability(65)));
    public static RegistryObject<Item> SHEEP_MILK_BUCKET = ITEMS.register("sheep_milk_bucket", ()-> new MilkBucketItem(new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> UNDERWATER_ARROW = ITEMS.register("underwater_arrow", ()-> new UnderwaterArrowItem(new Item.Properties()));
    public static RegistryObject<Item> DUCK_FEATHER = ITEMS.register("duck_feather", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> DUCK_EGG = ITEMS.register("duck_egg", ()-> new DuckEggItem(new Item.Properties().stacksTo(16)));

    //spawn eggs
    public static RegistryObject<Item> SHEEP_SPAWN_EGG = createSpawnEgg(EOTWEntities.SHEEP, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> PIG_SPAWN_EGG = createSpawnEgg(EOTWEntities.PIG, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> COW_SPAWN_EGG = createSpawnEgg(EOTWEntities.COW, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> MOOSHROOM_SPAWN_EGG = createSpawnEgg(EOTWEntities.MOOSHROOM, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> CHICKEN_SPAWN_EGG = createSpawnEgg(EOTWEntities.CHICKEN, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> DUCK_SPAWN_EGG = createSpawnEgg(EOTWEntities.DUCK, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> RABBIT_SPAWN_EGG = createSpawnEgg(EOTWEntities.RABBIT, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> HARE_SPAWN_EGG = createSpawnEgg(EOTWEntities.HARE, 0xFFFFFF, 0xFFFFFF);
    public static RegistryObject<Item> FERRET_SPAWN_EGG = createSpawnEgg(EOTWEntities.FERRET, 0xFFFFFF, 0xFFFFFF);
    // white like all the other eggs here, they use a custom sprite instead of the vanilla two layer
    // template and the colour handler multiplies layer0 by the background, so anything but white just
    // darkens the art
    public static RegistryObject<Item> RAT_SPAWN_EGG = createSpawnEgg(EOTWEntities.RAT, 0xFFFFFF, 0xFFFFFF);

    //tree
    public static RegistryObject<Item> VANILLA_FLOWER = ITEMS.register("vanilla_flower", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> VANILLA_STICK = ITEMS.register("vanilla_stick", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MANGO_SIGN = ITEMS.register("mango_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), EOTWBlocks.MANGO_SIGN.get(), EOTWBlocks.MANGO_WALL_SIGN.get()));
    public static final RegistryObject<Item> MANGO_HANGING_SIGN = ITEMS.register("mango_hanging_sign",
            () -> new HangingSignItem(EOTWBlocks.MANGO_HANGING_SIGN.get(), EOTWBlocks.MANGO_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> STRAWBERRY = ITEMS.register("strawberry",
            ()-> new ItemNameBlockItem(EOTWBlocks.STRAWBERRY_BUSH.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().fast().build())));

    public static final RegistryObject<Item> MANGO = ITEMS.register("mango",
            ()-> new BlockItem(EOTWBlocks.MANGO.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.2f).alwaysEat().fast().build())));

    public static RegistryObject<Item> RED_ONION = ITEMS.register("red_onion",
            ()-> new ItemNameBlockItem(EOTWBlocks.RED_ONION_CROP.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2f).build())));

    public static RegistryObject<Item> createIceCream(String name){
        return ITEMS.register(name + "_icecream", IceCream::new);
    }

    public static RegistryObject<Item> createSpawnEgg(RegistryObject<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor) {
        return ITEMS.register(type.getId().getPath() + "_spawn_egg",
                () -> new ForgeSpawnEggItem(type, backgroundColor, highlightColor, new Item.Properties()));
    }

    public static RegistryObject<Item> createEffectIceCream(String name, MobEffect effect){
        return ITEMS.register(name + "_icecream", ()-> new EffectIceCream(effect));
    }
}
