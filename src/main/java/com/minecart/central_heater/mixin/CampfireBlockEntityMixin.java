package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/entity/BlockEntityType;CAMPFIRE:Lnet/minecraft/world/level/block/entity/BlockEntityType;"))
    private static BlockEntityType<?> newBlockEntity(){
        return AllRegistry.burnable_campfire.get();
    }
}
