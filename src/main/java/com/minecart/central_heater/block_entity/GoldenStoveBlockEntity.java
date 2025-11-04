package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.FuelMap;
import com.minecart.central_heater.util.SoulFireState;
import com.minecart.central_heater.util.StackableItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GoldenStoveBlockEntity extends AbstractStoveBlockEntity {
    public int litTime;
    public SoulFireState litState;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public Item[] recordValidator;

    public final int fuelConsumption = 3;
    public final int soulFuelConsumption = 15;
    public static final int coolRate = 2;
    public static final int processSpeed = 600;

    public GoldenStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllRegistry.Red_nether_brick_stove_be.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0 || FuelMap.getSoulBurnTime(stack) != 0, 9);
        litTime = 0;
        litState = SoulFireState.NONE;
        cookingProgress = new int[itemCapacity];
        cookingTotalTime = new int[itemCapacity];
        recordValidator = new Item[itemCapacity];
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        litTime = tag.getInt("litTime");
        litState = SoulFireState.func.apply(tag.getString("litState"));
        cookingProgress = tag.getIntArray("cookingProgress");
        cookingTotalTime = tag.getIntArray("cookingTotalTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("litTime", litTime);
        tag.putString("litState", litState.getSerializedName());
        tag.putIntArray("cookingProgress", cookingProgress);
        tag.putIntArray("cookingTotalTime", cookingTotalTime);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.golden_stove");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity) {
        entity.updateFuel();

        if(!state.getValue(AllConstants.LIT_SOUL).equals(entity.litState)){
            entity.update();
        }
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

    public void dropContent() {
        for(int i = 0; i< fuels.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.5, this.getBlockPos().getZ()+0.5, this.fuels.getStackInSlot(i)));
        for(int i = 0; i< items.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.8, this.getBlockPos().getZ()+0.5, this.items.getStackInSlot(i)));
    }

    public void updateFuel(){
        boolean flag = this.isLit();
        if(this.litState.equals(SoulFireState.SOUL)){
            this.litTime-=soulFuelConsumption;
        }else if(this.litState.equals(SoulFireState.BURN)){
            this.litTime-=fuelConsumption;
        }
        if(flag && !this.isLit()){
            this.burnOneFuel();
        }
        if(this.litTime<0){
            this.litTime = 0;
            this.litState = SoulFireState.NONE;
        }
    }

    public void kindle() {
        if(this.isLit())
            return;
        burnOneFuel();
    }

    public boolean isLit() { return this.litTime > 0; }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || FuelMap.getBurnTime(stack) == 0)
            return;
        stack = fuels.extractItem(false);
        if(FuelMap.getSoulBurnTime(stack) != 0){
            this.litState = SoulFireState.SOUL;
            this.litTime += FuelMap.getSoulBurnTime(stack);
        }else{
            this.litState = SoulFireState.BURN;
            this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        }
        if (stack.hasCraftingRemainingItem()) {
            fuels.insertItem(stack.getCraftingRemainingItem(), false);
        }
    }

    public void update(){
        BlockState state = this.getBlockState();
        state = state.setValue(AllConstants.LIT_SOUL, this.litState);
        setChanged(this.level, this.getBlockPos(), state);
        this.level.setBlock(this.getBlockPos(), state, 3);
        getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_CLIENTS);
    }

}
