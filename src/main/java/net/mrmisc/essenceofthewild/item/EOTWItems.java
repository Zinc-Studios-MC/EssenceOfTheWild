package net.mrmisc.essenceofthewild.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.custom.EffectIceCream;
import net.mrmisc.essenceofthewild.item.custom.IceCream;

import java.util.function.Supplier;

public class EOTWItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EssenceOfTheWildMod.MOD_ID);

    //Icecream
    public static RegistryObject<Item> VANILLA_ICECREAM = createIceCream("vanilla");
    public static RegistryObject<Item> CHOCOLATE_ICECREAM = createIceCream("chocolate");
    public static RegistryObject<Item> STRAWBERRY_ICECREAM = createIceCream("strawberry");
    public static RegistryObject<Item> FIRE_RESISTANCE_ICECREAM = createEffectIceCream("fire_resistance", MobEffects.FIRE_RESISTANCE);
    public static RegistryObject<Item> JUMP_BOOST_ICECREAM = createEffectIceCream("jump_boost", MobEffects.JUMP);
    public static RegistryObject<Item> SPEED_ICECREAM = createEffectIceCream("speed", MobEffects.MOVEMENT_SPEED);

    //Items
    public static RegistryObject<Item> CONE = ITEMS.register("cone", ()-> new Item(new Item.Properties().stacksTo(8)));
    public static RegistryObject<Item> SHEEP_CHEESE = ITEMS.register("sheep_cheese", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> SHEEP_CHEESE_WEDGE = ITEMS.register("sheep_cheese_wedge", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> SHEEP_MILK_BUCKET = ITEMS.register("sheep_milk_bucket", ()-> new MilkBucketItem(new Item.Properties()));

    //Spawn Eggs
    public static RegistryObject<Item> SHEEP_SPAWN_EGG = createSpawnEgg(EOTWEntities.SHEEP, 15198183, 16758197);
    public static RegistryObject<Item> PIG_SPAWN_EGG = createSpawnEgg(EOTWEntities.PIG, 15771042, 14377823);
    public static RegistryObject<Item> COW_SPAWN_EGG = createSpawnEgg(EOTWEntities.COW, 4470310, 10592673);
    public static RegistryObject<Item> MOOSHROOM_SPAWN_EGG = createSpawnEgg(EOTWEntities.MOOSHROOM, 10489616, 12040119);
    public static RegistryObject<Item> CHICKEN_SPAWN_EGG = createSpawnEgg(EOTWEntities.CHICKEN, 10592673, 16711680);
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
