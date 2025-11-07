package com.minecart.central_heater.mixin;

import com.minecart.central_heater.util.FireState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin extends BlockEntity implements HopperBlockEntityDuckInterface{
    private CampfireBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Unique
    public static final int fuel_slots = 2;
    @Unique
    public static final int fuel_consumption = 1;
    @Unique
    private FireState litState = FireState.NONE;
    @Unique
    private int litTime = 0;
    @Unique
    private final NonNullList<ItemStack> fuels = NonNullList.withSize(fuel_slots, ItemStack.EMPTY);


    public FireState getLitState() {
        return litState;
    }

    public void setLitState(FireState litState) {
        this.litState = litState;
    }

    public int getLitTime() {
        return litTime;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }

    public ItemStack getFuel(int slot){
        return this.fuels.get(slot);
    }

    public void setFuel(ItemStack stack, int slot){
        this.fuels.set(slot, stack);
    }


    @Inject(method = "cookTick", at = @At(value = "HEAD"))
    public static void cookTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity) {

    }
}
