package com.minecart.central_heater.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.function.Function;

public class StackableItemStackHandler extends ItemStackHandler {
    public int maxSlotLimit;

    public void set(NonNullList<ItemStack> stacks){
        super.stacks = stacks;
    }

    public StackableItemStackHandler(int size, int maxSlotLimit){
        super(size);
        this.maxSlotLimit = maxSlotLimit;
    }

    @Override
    public int getSlotLimit(int slot) { return maxSlotLimit; }

    public NonNullList<ItemStack> getStacks(){
        return super.stacks;
    }

    public int countItem(){
        return this.stacks.stream().mapToInt(ItemStack::getCount).sum();
    }

    public int countStack(){ return this.stacks.stream().mapToInt(i -> i.isEmpty() ? 0 : 1).sum(); }

    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        for(int i=0;i<getSlots();i++){
            if(!ItemStack.matches(insertItem(i, stack.copy(), true), stack.copy())){
                if(!simulate) {
                    onContentsChanged(i);
                    ItemStack output = insertItem(i, stack.copy(), false);
                    stack.setCount(output.getCount());
                    return stack;
                }else{
                    return insertItem(i, stack.copy(), true);
                }
            }
        }
        return stack;
    }

    public ItemStack extractItem(int amount, boolean simulate) {
        for(int i=getSlots()-1;i>=0;i--){
            if(!extractItem(i, amount, true).isEmpty()){
                if(simulate)
                    return extractItem(i, amount, true);
                onContentsChanged(i);
                return getStackInSlot(i).split(amount);
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack extractItem(boolean simulate) {
        return extractItem(1, simulate);
    }

    public void setChanged(int slot) {
        super.onContentsChanged(slot);
    }
}
