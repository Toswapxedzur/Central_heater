package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlastOverheaterBlockEntity extends BlockEntity {
    public BlastOverheaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllRegistry.Blast_overheater_be.get(), pos, blockState);
        entity = null;
    }

    public int litTime;
    public int[] cookingProgress;
    public int[] cookingTotalTime;
    RandomizableContainerBlockEntity entity = null;
    public ItemStackHandler items = new ItemStackHandler(3){
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getBurnTime(RecipeType.SMELTING) != 0;
        }
    };

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.deserializeNBT(registries, tag.getCompound("items"));
        this.litTime = tag.getInt("LitTime");
        this.cookingProgress = tag.getIntArray("cookingProgress");
        this.cookingTotalTime = tag.getIntArray("cookingTotalTime");
        entity = null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("items", items.serializeNBT(registries));
        tag.putInt("LitTime", this.litTime);
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return super.getUpdateTag(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    public void update(){
        BlockState state = getBlockState();
        state.setValue(BlockStateProperties.LIT, Boolean.valueOf(isLit()));
        level.setBlock(getBlockPos(), state, 3);
        setChanged(level, getBlockPos(), state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlastOverheaterBlockEntity entity) {
        if(entity.entity == null || entity.entity.isRemoved()){
            if(findBlockEntity(level, pos) == null) {
                level.destroyBlock(pos, false);
            }else{
                entity.entity = findBlockEntity(level, pos);
                entity.cookingProgress = new int[entity.entity.getContainerSize()];
                entity.cookingTotalTime = new int[entity.entity.getContainerSize()];
            }
        }

        if(state.getValue(BlockStateProperties.LIT) != entity.isLit()){
            entity.update();
        }
    }


    public static void insertItem(BlastOverheaterBlockEntity entity, ItemStack stack) {
        for(int j=0; j<3; j++){
            if(entity.items.insertItem(j, stack, true).getCount() != stack.getCount()){
                entity.items.insertItem(j, stack.split(1), false);
                break;
            }
        }
    }

    public static ItemStack extractItem(BlastOverheaterBlockEntity entity) {
        for(int j=0; j<3; j++){
            if(entity.items.extractItem(j, 1, true) != ItemStack.EMPTY){
                return entity.items.extractItem(j, 1, false);
            }
        }
        return ItemStack.EMPTY;
    }

    public static void dropContent(BlastOverheaterBlockEntity entity) {
        for(int i=0; i<3; i++){
            entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getBlockPos().getX()+0.5, entity.getBlockPos().getY()+0.2, entity.getBlockPos().getZ()+0.5, entity.items.getStackInSlot(i)));
        }
    }

    public static void kindle(BlastOverheaterBlockEntity entity) {
        for(int i=0; i<3; i++){
            if(entity.items.getStackInSlot(i).getBurnTime(RecipeType.SMELTING) > 0){
                entity.litTime += entity.items.getStackInSlot(i).getBurnTime(RecipeType.SMELTING);
                entity.items.getStackInSlot(i).shrink(1);
                entity.update();
            }
        }
    }

    public static RandomizableContainerBlockEntity findBlockEntity(Level level, BlockPos pos){
        if(level.getBlockEntity(pos.below()) == null || level.getBlockEntity(pos.below()).isRemoved() ||
                !(level.getBlockEntity(pos.below()) instanceof RandomizableContainerBlockEntity)){
            return null;
        }else{
            return (RandomizableContainerBlockEntity) level.getBlockEntity(pos.below());
        }
    }
}
