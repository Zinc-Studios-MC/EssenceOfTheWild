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
        basicItem(EOTWItems.MANGO.get());
        basicItem(EOTWItems.SHEEP_CHEESE.get());
        basicItem(EOTWItems.SHEEP_CHEESE_WEDGE.get());
        basicItem(EOTWItems.SHEEP_MILK_BUCKET.get());
        basicItem(EOTWItems.VANILLA_ICECREAM.get());
        basicItem(EOTWItems.STRAWBERRY_ICECREAM.get());
        basicItem(EOTWItems.CHOCOLATE_ICECREAM.get());
        basicItem(EOTWItems.FIRE_RESISTANCE_ICECREAM.get());
        basicItem(EOTWItems.SPEED_ICECREAM.get());
        basicItem(EOTWItems.JUMP_BOOST_ICECREAM.get());

        basicItem(EOTWItems.VANILLA_STICK.get());
        basicItem(EOTWItems.VANILLA_FLOWER.get());
        basicItem(EOTWItems.MANGO_SIGN.get());
        basicItem(EOTWItems.MANGO_HANGING_SIGN.get());

        basicItem(EOTWItems.STRAWBERRY.get());
        basicItem(EOTWItems.RED_ONION.get());
        basicItem(EOTWItems.ICE_CUBES.get());
        basicItem(EOTWItems.ICE_AXE.get());
        basicItem(EOTWItems.UNDERWATER_ARROW.get());
    }
}
