package com.minecart.central_heater.recipe;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class SeethingRecipe extends AbstractCookingRecipe{
    RecipeSerializer<SeethingRecipe> SEETHING_RECIPE = RecipeSerializer.register("seething", new SimpleCookingSerializer<>(SeethingRecipe::new, 400));

    public SeethingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(AllRegistry.Seething.get(), group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SEETHING_RECIPE;
    }
}
