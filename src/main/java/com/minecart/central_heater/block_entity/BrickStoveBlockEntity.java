package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block.BrickStoveBlock;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.util.FireState;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BrickStoveBlockEntity extends AbstractStoveBlockEntity{
    public int litTime;
    public FireState litState;
    public FireState litStateValidator;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> recordValidator;

    public final int fuelRate = 3;
    public static final int coolRate = 2;
    public static final int processSpeed = 600;

    public BrickStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllRegistry.brick_stove_be.get(), pos, blockState, 2, stack -> stack.getBurnTime(RecipeType.SMELTING) != 0, 4);
        litState = FireState.NONE;
        litTime = 0;
        litStateValidator = FireState.NONE;
        cookingProgress = new int[itemCapacity];
        cookingTotalTime = new int[itemCapacity];
        recordValidator = NonNullList.withSize(itemCapacity, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.litState = FireState.getFireState(tag.getBoolean("litState"));
        this.litTime = tag.getInt("LitTime");
        this.litStateValidator = FireState.getFireState(tag.getBoolean("litStateValidator"));
        this.cookingProgress = tag.getIntArray("cookingProgress");
        this.cookingTotalTime = tag.getIntArray("cookingTotalTime");
        ContainerHelper.loadAllItems(tag.getCompound("validator"), recordValidator, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putInt("LitTime", this.litTime);
        tag.putBoolean("litStateValidator", this.litStateValidator.getBooleanState());
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag validatorTag = new CompoundTag();
        ContainerHelper.saveAllItems(validatorTag, recordValidator, registries);
        tag.put("validator", validatorTag);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.brick_stove");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BrickStoveBlockEntity entity){
        entity.updateFuel();

        for (int i = 0; i < entity.itemCapacity; i++) {
            if(!entity.isLit())
                entity.cookingProgress[i] = Math.max(0, entity.cookingProgress[i] - coolRate);
            if (ItemStack.matches(entity.items.getStackInSlot(i), entity.recordValidator.get(i))) {
                if(entity.isLit())
                    entity.cookingProgress[i] += 1;
            }else if(ItemStack.isSameItemSameComponents(entity.items.getStackInSlot(i), entity.recordValidator.get(i))){
                entity.cookingTotalTime[i] = entity.items.getStackInSlot(i).getCount() * processSpeed;
            }else{
                entity.cookingProgress[i] = 0;
                entity.cookingTotalTime[i] = entity.items.getStackInSlot(i).getCount() * processSpeed;
            }
            entity.recordValidator.set(i, entity.items.getStackInSlot(i).copy());
        }

        for (int i = 0; i < entity.itemCapacity; i++) {
            if (entity.cookingTotalTime[i] != 0 && entity.cookingProgress[i] >= entity.cookingTotalTime[i] && !entity.items.getStackInSlot(i).isEmpty()) {
                RecipeHolder<CampfireCookingRecipe> recipeHolder = level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(entity.items.getStackInSlot(i)), level).orElse(null);
                if (entity.burn(i, recipeHolder, level.registryAccess())) {
                    entity.cookingProgress[i] = 0;
                }
            }
        }

        if(state.getValue(BrickStoveBlock.LIT) != entity.isLit()){
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

    public void updateFuel(){
        if(!this.litState.equals(FireState.NONE)){
            if(this.litTime<=fuelRate)
                burnOneFuel();
        }
        this.litTime -= fuelRate;
        if(litTime<=0){
            this.litState = FireState.NONE;
            this.litTime = 0;
        }
    }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0)
            return;
        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if (stack.hasCraftingRemainingItem()) {
            fuels.insertItem(stack.getCraftingRemainingItem(), false);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean burn(int slot, @Nullable RecipeHolder<CampfireCookingRecipe> recipe, RegistryAccess access){
        if(recipe!=null){
            ItemStack stack = items.getStackInSlot(slot);
            ItemStack stack1 = recipe.value().assemble(new SingleRecipeInput(stack), access).copy();
            stack1.setCount(stack.getCount());
            items.setStackInSlot(slot, stack1);
            return true;
        }
        return false;
    }

    public boolean isLit() { return this.litState.equals(FireState.LIT); }

    @Override
    public void update() {
        BlockState state = this.getBlockState();
        state = state.setValue(BrickStoveBlock.LIT, Boolean.valueOf(isLit()));
        setChanged(this.level, this.getBlockPos(), state);
        this.level.setBlock(this.getBlockPos(), state, 3);
        getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_CLIENTS);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

}
