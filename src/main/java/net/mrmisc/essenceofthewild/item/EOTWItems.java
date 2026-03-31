package net.mrmisc.essenceofthewild.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.custom.EffectIceCream;
import net.mrmisc.essenceofthewild.item.custom.IceCream;

public class EOTWItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<Item> VANILLA_ICECREAM = createIceCream("vanilla");
    public static RegistryObject<Item> CHOCOLATE_ICECREAM = createIceCream("chocolate");
    public static RegistryObject<Item> STRAWBERRY_ICECREAM = createIceCream("strawberry");
    public static RegistryObject<Item> FIRE_RESISTANCE_ICECREAM = createEffectIceCream("fire_resistance", MobEffects.FIRE_RESISTANCE);
    public static RegistryObject<Item> JUMP_BOOST_ICECREAM = createEffectIceCream("jump_boost", MobEffects.JUMP);
    public static RegistryObject<Item> SPEED_ICECREAM = createEffectIceCream("speed", MobEffects.MOVEMENT_SPEED);

    public static RegistryObject<Item> CONE = ITEMS.register("cone", ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> SHEEP_SPAWN_EGG = ITEMS.register("sheep_spawn_egg",
            ()-> new ForgeSpawnEggItem(EOTWEntities.SHEEP,15198183, 16758197, new Item.Properties()));

    public static RegistryObject<Item> PIG_SPAWN_EGG = ITEMS.register("pig_spawn_egg",
            ()-> new ForgeSpawnEggItem(EOTWEntities.PIG,15771042, 14377823, new Item.Properties()));
    public static RegistryObject<Item> createIceCream(String name){
        return ITEMS.register(name + "_icecream", IceCream::new);
    }

    public static RegistryObject<Item> createEffectIceCream(String name, MobEffect effect){
        return ITEMS.register(name + "_icecream", ()-> new EffectIceCream(effect));
    }
}
