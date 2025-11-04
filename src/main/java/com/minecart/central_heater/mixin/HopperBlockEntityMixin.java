package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Redirect(method = "tryMoveInItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"))
    private static int getMaxStackSize(ItemStack stack, @Local(ordinal = 1, argsOnly = true) Container destination){
        return Math.min(stack.getMaxStackSize(), destination.getMaxStackSize(stack));
    }
}
