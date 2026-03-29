package net.mrmisc.essenceofthewild.item.custom;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class IceCream extends Item {
    public IceCream() {
        super(new Properties().stacksTo(8).food(new FoodProperties.Builder()
                .nutrition(1)
                .saturationMod(0.2f)
                .fast()
                .build()));
    }
}
