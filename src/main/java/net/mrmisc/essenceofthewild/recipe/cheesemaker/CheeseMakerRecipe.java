package net.mrmisc.essenceofthewild.recipe.cheesemaker;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.config.EOTWConfig;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;
import org.jetbrains.annotations.Nullable;

public class CheeseMakerRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final ItemStack container;
    private final int processTicks;

    public CheeseMakerRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, ItemStack container, int processTicks) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.container = container;
        this.processTicks = Math.max(0, processTicks);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(EOTWBlocks.CHEESE_MAKER.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EOTWRecipes.CHEESE_MAKER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EOTWRecipes.CHEESE_MAKER_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getContainer() {
        return container.copy();
    }

    public int getProcessTicks() {
        return processTicks > 0 ? processTicks : EOTWConfig.cheeseMakerProcessTicks();
    }

    public static class Serializer implements RecipeSerializer<CheeseMakerRecipe> {
        @Override
        public CheeseMakerRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            ItemStack container = json.has("container")
                    ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "container"))
                    : ItemStack.EMPTY;
            int processTicks = getProcessTicks(json);
            return new CheeseMakerRecipe(id, ingredient, result, container, processTicks);
        }

        @Override
        public @Nullable CheeseMakerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            ItemStack container = buffer.readItem();
            int processTicks = buffer.readVarInt();
            return new CheeseMakerRecipe(id, ingredient, result, container, processTicks);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CheeseMakerRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeItem(recipe.container);
            buffer.writeVarInt(recipe.processTicks);
        }

        private static int getProcessTicks(JsonObject json) {
            if (json.has("process_ticks")) {
                return Math.max(1, GsonHelper.getAsInt(json, "process_ticks"));
            }

            if (json.has("process_seconds")) {
                return Math.max(1, GsonHelper.getAsInt(json, "process_seconds") * 20);
            }

            return 0;
        }
    }
}
