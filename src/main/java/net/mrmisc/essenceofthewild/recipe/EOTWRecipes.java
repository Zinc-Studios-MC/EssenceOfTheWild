package net.mrmisc.essenceofthewild.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.recipe.cheesemaker.CheeseMakerRecipe;
import net.mrmisc.essenceofthewild.recipe.freezer.WoodenFreezerRecipe;

public class EOTWRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EssenceOfTheWildMod.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, EssenceOfTheWildMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<WoodenFreezerRecipe>> WOODEN_FREEZER_SERIALIZER =
            RECIPE_SERIALIZERS.register("wooden_freezer", WoodenFreezerRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<WoodenFreezerRecipe>> WOODEN_FREEZER_TYPE =
            RECIPE_TYPES.register("wooden_freezer", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return EssenceOfTheWildMod.MOD_ID + ":wooden_freezer";
                }
            });

    public static final RegistryObject<RecipeSerializer<CheeseMakerRecipe>> CHEESE_MAKER_SERIALIZER =
            RECIPE_SERIALIZERS.register("cheese_maker", CheeseMakerRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<CheeseMakerRecipe>> CHEESE_MAKER_TYPE =
            RECIPE_TYPES.register("cheese_maker", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return EssenceOfTheWildMod.MOD_ID + ":cheese_maker";
                }
            });
}
