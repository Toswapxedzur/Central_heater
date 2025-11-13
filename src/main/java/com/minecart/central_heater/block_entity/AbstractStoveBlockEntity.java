package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.util.StackableItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class AbstractStoveBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    //fuel before the item
    public final int fuelCapacity;
    public final StackableItemStackHandler fuels;
    public final int itemCapacity;
    public final StackableItemStackHandler items;
    public final int[] fuelSlot;
    public final int[] itemSlot;

    public AbstractStoveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int fuelCapacity, Predicate<ItemStack> isFuelValid, int itemCapacity) {
        super(type, pos, blockState);
        this.fuelCapacity = fuelCapacity;
        this.fuels = new StackableItemStackHandler(fuelCapacity, 1){
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return isFuelValid.test(stack);
            }
            @Override
            protected void onContentsChanged(int slot) { update(); }
        };
        this.fuelSlot = IntStream.range(0, fuelCapacity).toArray();
        this.itemCapacity = itemCapacity;
        this.items = new StackableItemStackHandler(itemCapacity, 1){
            @Override
            protected void onContentsChanged(int slot) { update(); }
        };
        this.itemSlot = IntStream.range(fuelCapacity, fuelCapacity + itemCapacity).toArray();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.deserializeNBT(registries, tag.getCompound("items"));
        fuels.deserializeNBT(registries, tag.getCompound("fuels"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("items", items.serializeNBT(registries));
        tag.put("fuels", fuels.serializeNBT(registries));
    }

    public abstract void update();



    @Override
    public int getContainerSize() {
        return fuelCapacity + itemCapacity;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : fuels.getStacks())
            if(!stack.isEmpty())
                return false;
        for(ItemStack stack : items.getStacks())
            if(!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if(slot >= fuelCapacity)
            return items.getStackInSlot(slot - fuelCapacity);
        return fuels.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        this.setChanged();
        if(slot >= fuelCapacity)
            return items.extractItem(slot - fuelCapacity, amount, false);
        return fuels.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if(slot >= fuelCapacity)
            return items.extractItem(slot - fuelCapacity, Item.ABSOLUTE_MAX_STACK_SIZE, false);
        return fuels.extractItem(slot, Item.ABSOLUTE_MAX_STACK_SIZE, false);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if(slot >= fuelCapacity) {
            items.setStackInSlot(slot - fuelCapacity, stack);
            return;
        }
        fuels.setStackInSlot(slot, stack);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side){
            case UP -> {
                return itemSlot;
            }
            case DOWN -> {
                return itemSlot;
            }
            default -> {
                return fuelSlot;
            }
        }
    }

    @Override
    public int getMaxStackSize() {
        return Math.min(fuels.maxSlotLimit, items.maxSlotLimit);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        boolean flag = false;
        for(int i : getSlotsForFace(direction))
            if(i == index)
                flag = true;
        if(!flag)
            return false;

        if(index < fuelCapacity){
            return fuels.isItemValid(index, itemStack);
        }else{
            return items.isItemValid(index - fuelCapacity, itemStack);
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return Arrays.stream(itemSlot).anyMatch(i -> i == index);
    }



    @Override
    public NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
    }
}
