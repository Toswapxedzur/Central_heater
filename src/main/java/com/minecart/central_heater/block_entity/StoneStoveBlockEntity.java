package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.util.StackableItemStackHandler;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class StoneStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public Item[] recordValidator;

    public final int fuelConsumption = 3;
    public static final int coolRate = 2;
    public static final int processSpeed = 600;

    public StoneStoveBlockEntity(BlockPos pos, BlockState blockState){
        super(AllRegistry.Stone_stove_be.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0, 4);
        litTime = 0;
        cookingProgress = new int[itemCapacity];
        cookingTotalTime = new int[itemCapacity];
        recordValidator = new Item[itemCapacity];
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.deserializeNBT(registries, tag.getCompound("items"));
        fuels.deserializeNBT(registries, tag.getCompound("fuels"));
        this.litTime = tag.getInt("LitTime");
        this.cookingProgress = tag.getIntArray("cookingProgress");
        this.cookingTotalTime = tag.getIntArray("cookingTotalTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("items", items.serializeNBT(registries));
        tag.put("fuels", fuels.serializeNBT(registries));
        tag.putInt("LitTime", this.litTime);
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.stone_stove");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StoneStoveBlockEntity entity) {
        entity.updateFuel();

        for (int i = 0; i < entity.itemCapacity; i++) {
            if (entity.items.getStackInSlot(i).isEmpty()) {
                entity.cookingProgress[i] = 0;
            } else if (!entity.items.getStackInSlot(i).is(entity.recordValidator[i])) {
                entity.cookingProgress[i] = 0;
                entity.cookingTotalTime[i] = entity.items.getStackInSlot(i).getCount() * processSpeed;
            } else {
                entity.cookingTotalTime[i] = entity.items.getStackInSlot(i).getCount() * processSpeed;
            }
            entity.recordValidator[i] = entity.items.getStackInSlot(i).copy().getItem();
        }

        RecipeHolder<? extends AbstractCookingRecipe> recipeHolder;
        for (int i = 0; i < entity.itemCapacity; i++) {
            if (entity.isLit()) {
                entity.cookingProgress[i] += 1;
                if (entity.cookingProgress[i] >= entity.cookingTotalTime[i] && !entity.items.getStackInSlot(i).isEmpty()) {
                    recipeHolder = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(entity.items.getStackInSlot(i)), level).orElse(null);
                    if (entity.burn(i, recipeHolder, level.registryAccess())) {
                        entity.cookingProgress[i] = 0;
                    }
                }
            } else {
                entity.cookingProgress[i] = Math.max(0, entity.cookingProgress[i] - coolRate);
            }
        }

        if(state.getValue(StoneStoveBlock.LIT) != entity.isLit()){
            entity.update();
        }
    }

    public void dropContent() {
        for(int i = 0; i< fuels.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.5, this.getBlockPos().getZ()+0.5, this.fuels.getStackInSlot(i)));
        for(int i = 0; i< items.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.8, this.getBlockPos().getZ()+0.5, this.items.getStackInSlot(i)));
    }

    public void kindle() {
        if(this.isLit())
            return;
        burnOneFuel();
    }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0)
            return;
        stack = fuels.extractItem(false);
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if (stack.hasCraftingRemainingItem()) {
            fuels.insertItem(stack.getCraftingRemainingItem(), false);
        }
    }

    public boolean canBurn(int slot, @Nullable RecipeHolder<? extends AbstractCookingRecipe> recipe, RegistryAccess access){
        if(recipe!=null && slot>=0 && slot<itemCapacity){
            ItemStack stack = recipe.value().assemble(new SingleRecipeInput(items.getStackInSlot(slot)), access);
            return !stack.isEmpty();
        }
        return false;
    }

    public boolean burn(int slot, @Nullable RecipeHolder<? extends AbstractCookingRecipe> recipe, RegistryAccess access){
        if(recipe!=null && canBurn(slot, recipe, access)){
            ItemStack stack = items.getStackInSlot(slot);
            ItemStack stack1 = recipe.value().assemble(new SingleRecipeInput(stack), access).copy();
            stack1.setCount(stack.getCount());
            items.setStackInSlot(slot, stack1);
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    public void updateFuel(){
        if(this.litTime<0){
            this.litTime = 0;
        }else if(this.litTime<=fuelConsumption){
            this.burnOneFuel();
        }
        this.litTime-=fuelConsumption;
    }

    public boolean isLit() { return this.litTime > 0; }

    public void update(){
        BlockState state = this.getBlockState();
        state = state.setValue(BlockStateProperties.LIT, Boolean.valueOf(this.isLit()));
        setChanged(this.level, this.getBlockPos(), state);
        this.level.setBlock(this.getBlockPos(), state, 3);
        getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_CLIENTS);
    }
}
