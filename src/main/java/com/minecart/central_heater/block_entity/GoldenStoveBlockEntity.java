package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block.BrickStoveBlock;
import com.minecart.central_heater.block.GoldenStoveBlock;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.FuelMap;
import com.minecart.central_heater.util.SoulFireState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GoldenStoveBlockEntity extends AbstractStoveBlockEntity {
    public int litTime;
    public SoulFireState litState;
    public SoulFireState litStateValidator;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> recordValidator;

    public final int fuelConsumption = 3;
    public final int soulFuelConsumption = 7;
    public static final int coolRate = 2;
    public static final int processSpeed = 400;

    public GoldenStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllRegistry.red_nether_brick_stove_be.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0 || FuelMap.getSoulBurnTime(stack) != 0, 9);
        litState = SoulFireState.NONE;
        litTime = 0;
        litStateValidator = SoulFireState.NONE;
        cookingProgress = new int[itemCapacity];
        cookingTotalTime = new int[itemCapacity];
        recordValidator = NonNullList.withSize(itemCapacity, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        litTime = tag.getInt("litTime");
        litState = SoulFireState.func.apply(tag.getString("litState"));
        litStateValidator = SoulFireState.func.apply(tag.getString("litStateValidator"));
        cookingProgress = tag.getIntArray("cookingProgress");
        cookingTotalTime = tag.getIntArray("cookingTotalTime");
        ContainerHelper.loadAllItems(tag.getCompound("validator"), recordValidator, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("litTime", litTime);
        tag.putString("litState", litState.getSerializedName());
        tag.putString("litStateValidator", litStateValidator.getSerializedName());
        tag.putIntArray("cookingProgress", cookingProgress);
        tag.putIntArray("cookingTotalTime", cookingTotalTime);
        CompoundTag validatorTag = new CompoundTag();
        ContainerHelper.saveAllItems(validatorTag, recordValidator, registries);
        tag.put("validator", validatorTag);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.golden_stove");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity) {
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
            if(entity.litState.equals(SoulFireState.SOUL) && !entity.litStateValidator.equals(SoulFireState.SOUL)) {
                entity.cookingProgress[i] = 0;
            }
            entity.recordValidator.set(i, entity.items.getStackInSlot(i).copy());
        }
        entity.litStateValidator = entity.litState;

        for (int i = 0; i < entity.itemCapacity; i++) {
            if (entity.cookingTotalTime[i] != 0 && entity.cookingProgress[i] >= entity.cookingTotalTime[i] && !entity.items.getStackInSlot(i).isEmpty()) {
                RecipeHolder<? extends AbstractCookingRecipe> recipeHolder;
                if(entity.litState.equals(SoulFireState.SOUL))
                    recipeHolder = level.getRecipeManager().getRecipeFor(AllRegistry.Seething.get(), new SingleRecipeInput(entity.items.getStackInSlot(i)), level).orElse(null);
                else
                    recipeHolder = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(entity.items.getStackInSlot(i)), level).orElse(null);
                if (entity.burn(i, recipeHolder, level.registryAccess())) {
                    entity.cookingProgress[i] = 0;
                }
            }
        }

        if(!state.getValue(AllConstants.LIT_SOUL).equals(entity.litState)){
            entity.update();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity){
        RandomSource randomsource = level.random;
        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(SoulFireState.NONE)) {
            if (randomsource.nextFloat() < 0.11F) {
                for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,true,
                            (double)pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            (double)pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                            (double)pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            0.0, 0.07, 0.0);
                }
            }
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

    public boolean burn(int slot, @Nullable RecipeHolder<? extends AbstractCookingRecipe> recipe, RegistryAccess access){
        if(recipe!=null){
            ItemStack stack = items.getStackInSlot(slot);
            ItemStack stack1 = recipe.value().assemble(new SingleRecipeInput(stack), access).copy();
            stack1.setCount(stack.getCount());
            items.setStackInSlot(slot, stack1);
            return true;
        }
        return false;
    }

    public void update(){
        BlockState state = this.getBlockState();
        state = state.setValue(AllConstants.LIT_SOUL, this.litState);
        setChanged(this.level, this.getBlockPos(), state);
        this.level.setBlock(this.getBlockPos(), state, 3);
        getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_CLIENTS);
    }

}
