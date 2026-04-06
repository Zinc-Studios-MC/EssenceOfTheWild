package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class EOTWItemModelProvider extends ItemModelProvider {
    public EOTWItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EssenceOfTheWildMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(EOTWItems.CONE.get());
        basicItem(EOTWItems.SHEEP_CHEESE.get());
        basicItem(EOTWItems.SHEEP_CHEESE_WEDGE.get());
        basicItem(EOTWItems.SHEEP_MILK_BUCKET.get());
        basicItem(EOTWItems.VANILLA_ICECREAM.get());
        basicItem(EOTWItems.STRAWBERRY_ICECREAM.get());
        basicItem(EOTWItems.CHOCOLATE_ICECREAM.get());
        basicItem(EOTWItems.FIRE_RESISTANCE_ICECREAM.get());
        basicItem(EOTWItems.SPEED_ICECREAM.get());
        basicItem(EOTWItems.JUMP_BOOST_ICECREAM.get());
    }
}
