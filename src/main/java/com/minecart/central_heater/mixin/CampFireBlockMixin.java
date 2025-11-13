package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block_entity.BurnableCampfireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Debug(export = true)
@Mixin(CampfireBlock.class)
public abstract class CampFireBlockMixin extends BaseEntityBlock {
    private CampFireBlockMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 0))
    private Boolean valueOf(boolean value){
        return Boolean.valueOf(false);
    }

    @Redirect(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 2))
    private Boolean valueOf2(boolean value){
        return false;
    }

    @Overwrite
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnableCampfireBlockEntity(pos, state);
    }

    @Overwrite
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity campfireblockentity) {
            ItemStack itemStack = player.getItemInHand(hand);
            Optional<RecipeHolder<CampfireCookingRecipe>> optional = campfireblockentity.getCookableRecipe(itemStack);
            if (!level.isClientSide) {
                if(itemStack.is(Items.FLINT_AND_STEEL)){
                    campfireblockentity.kindle();
                    stack.hurtAndBreak(1, (ServerLevel) level, player, item -> {});
                    return ItemInteractionResult.SUCCESS;
                }
                if(hitResult.getDirection().equals(Direction.UP) && campfireblockentity.fuels.isItemValid(0, stack)){
                    campfireblockentity.addFuel(stack, false);
                    return ItemInteractionResult.SUCCESS;
                }
                if (optional.isPresent() && campfireblockentity.placeFood(player, itemStack, ((CampfireCookingRecipe)((RecipeHolder)optional.get()).value()).getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Overwrite
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return state.getValue(CampfireBlock.LIT) ? createTickerHelper(blockEntityType, AllRegistry.burnable_campfire.get(), BurnableCampfireBlockEntity::particleTick) : null;
        } else {
            return state.getValue(CampfireBlock.LIT)
                    ? createTickerHelper(blockEntityType, AllRegistry.burnable_campfire.get(), BurnableCampfireBlockEntity::cookTick)
                    : createTickerHelper(blockEntityType, AllRegistry.burnable_campfire.get(), BurnableCampfireBlockEntity::cooldownTick);
        }
    }

    @Overwrite
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity blockentity) {
                Containers.dropContents(level, pos, blockentity.getFuels());
                Containers.dropContents(level, pos, blockentity.getItems());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
