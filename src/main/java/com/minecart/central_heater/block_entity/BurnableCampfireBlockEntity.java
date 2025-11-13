package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.util.FireState;
import com.minecart.central_heater.util.StackableItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BurnableCampfireBlockEntity extends CampfireBlockEntity {
    public static final int fuel_slots = 2;
    public int litTime;
    public int campfireLitTime;
    public final StackableItemStackHandler fuels = new StackableItemStackHandler(fuel_slots, 1){
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getBurnTime(RecipeType.SMELTING) > 0;
        }
    };

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        litTime = tag.getInt("litTime");
        campfireLitTime = tag.getInt("campfireLitTime");
        fuels.deserializeNBT(registries, tag.getCompound("fuels"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("litTime", litTime);
        tag.putInt("campfireLitTime", campfireLitTime);
        tag.put("fuels", fuels.serializeNBT(registries));
    }

    public BurnableCampfireBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        litTime = 0;
        campfireLitTime = 200;
    }

    public int getLitTime() {
        return litTime;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }

    public int getCampfireLitTime(){
        return campfireLitTime;
    }

    public void setCampfireLitTime(int lit){
        campfireLitTime = lit;
    }

    public ItemStack getFuel(int slot){
        return this.fuels.getStackInSlot(slot);
    }

    public void setFuel(int slot, ItemStack stack){
        this.fuels.setStackInSlot(slot, stack);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public NonNullList<ItemStack> getFuels(){
        return this.fuels.getStacks();
    }

    public void consumeFuel(int amount){
        litTime -= amount;
        if(litTime < 0){
            campfireLitTime += litTime;
            litTime = 0;
        }
    }

    public boolean isLit(){
        return getBlockState().getValue(CampfireBlock.LIT).booleanValue();
    }

    public void setLit(boolean lit){
        if(!level.isClientSide)
            level.setBlock(getBlockPos(), getBlockState().setValue(CampfireBlock.LIT, Boolean.valueOf(lit)), 11);
    }

    public boolean addFuel(ItemStack stack, boolean simulate){
        for(int i=0;i<fuel_slots;i++){
            if(getFuel(i).isEmpty()){
                setFuel(i, stack.split(1));
                return true;
            }
        }
        return false;
    }

    public ItemStack takeFuel(boolean simulate){
        for(int i=fuel_slots-1;i>=0;i--){
            if(!getFuel(i).isEmpty()){
                ItemStack ret = getFuel(i).copy();
                if(!simulate)
                    setFuel(i, ItemStack.EMPTY);
                return ret;
            }
        }
        return ItemStack.EMPTY;
    }

    public void burnFuel(){
        ItemStack stack = takeFuel(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0)
            return;
        stack = takeFuel(false);
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if(stack.hasCraftingRemainingItem())
            addFuel(stack.getCraftingRemainingItem(), false);
    }


    public static void cookTick(Level level, BlockPos pos, BlockState state, BurnableCampfireBlockEntity blockEntity) {
        blockEntity.consumeFuel(1);
        if (blockEntity.litTime <= 0) {
            blockEntity.burnFuel();
        }
        if (blockEntity.campfireLitTime <= 0) {
            level.destroyBlock(pos, true);
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
            level.playLocalSound(pos, SoundEvents.GENERIC_BURN, SoundSource.BLOCKS, 1f, 1f, true);
        }

        CampfireBlockEntity.cookTick(level, pos, state, blockEntity);
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, BurnableCampfireBlockEntity blockEntity) {
        blockEntity.litTime = 0;

        CampfireBlockEntity.cooldownTick(level, pos, state, (CampfireBlockEntity) blockEntity);
    }

    public void kindle(){
        if(!isLit())
            burnFuel();
        setLit(true);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void clearContent() {
        this.fuels.getStacks().clear();
        super.clearContent();
    }
}
