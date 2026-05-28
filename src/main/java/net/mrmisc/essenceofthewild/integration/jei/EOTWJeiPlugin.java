package net.mrmisc.essenceofthewild.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;
import net.mrmisc.essenceofthewild.recipe.cheesemaker.CheeseMakerRecipe;
import net.mrmisc.essenceofthewild.recipe.freezer.WoodenFreezerRecipe;

import java.util.List;

@JeiPlugin
public class EOTWJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(EssenceOfTheWildMod.MOD_ID, "jei");
    public static final RecipeType<WoodenFreezerRecipe> WOODEN_FREEZER =
            RecipeType.create(EssenceOfTheWildMod.MOD_ID, "wooden_freezer", WoodenFreezerRecipe.class);
    public static final RecipeType<CheeseMakerRecipe> CHEESE_MAKER =
            RecipeType.create(EssenceOfTheWildMod.MOD_ID, "cheese_maker", CheeseMakerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WoodenFreezerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CheeseMakerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        List<WoodenFreezerRecipe> freezerRecipes = minecraft.level.getRecipeManager().getAllRecipesFor(EOTWRecipes.WOODEN_FREEZER_TYPE.get());
        List<CheeseMakerRecipe> cheeseMakerRecipes = minecraft.level.getRecipeManager().getAllRecipesFor(EOTWRecipes.CHEESE_MAKER_TYPE.get());

        registration.addRecipes(WOODEN_FREEZER, freezerRecipes);
        registration.addRecipes(CHEESE_MAKER, cheeseMakerRecipes);
        registration.addIngredientInfo(new ItemStack(EOTWItems.SHEEP_MILK_BUCKET.get()), VanillaTypes.ITEM_STACK, net.minecraft.network.chat.Component.translatable("jei.essenceofthewild.info.sheep_milk_bucket"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(EOTWBlocks.WOODEN_FREEZER.get()), WOODEN_FREEZER);
        registration.addRecipeCatalyst(new ItemStack(EOTWBlocks.CHEESE_MAKER.get()), CHEESE_MAKER);
    }
}
