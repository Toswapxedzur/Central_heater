package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Debug(export = true)
@Mixin(SimpleCookingRecipeBuilder.class)
public abstract class SimpleCookingRecipeBuilderMixin {

    @Inject(method = "determineRecipeCategory", at = @At(value = "HEAD"), cancellable = true)
    private static void determineRecipeCategory(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike result, CallbackInfoReturnable<CookingBookCategory> returnable) {
        if (serializer == AllRegistry.Seething_ser.get()) {
            if (result.asItem().components().has(DataComponents.FOOD)) {
                returnable.setReturnValue(CookingBookCategory.FOOD);
            } else {
                returnable.setReturnValue(result.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC);
            }
        }
    }
}
