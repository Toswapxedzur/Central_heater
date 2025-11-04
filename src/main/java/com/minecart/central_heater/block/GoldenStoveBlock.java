package com.minecart.central_heater.block;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block_entity.GoldenStoveBlockEntity;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.SoulFireState;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GoldenStoveBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<SoulFireState> LIT_SOUL = AllConstants.LIT_SOUL;

    public static final VoxelShape SHAPE = box(0,0,0,16,16,16);

    public static final MapCodec<GoldenStoveBlock> CODEC = simpleCodec(GoldenStoveBlock::new);

    public GoldenStoveBlock(Properties properties) {
        super(properties.noOcclusion().lightLevel(lit -> {
            if(lit.getValue(AllConstants.LIT_SOUL).getState() == 2){ return 15; }
            else if(lit.getValue(AllConstants.LIT_SOUL).getState() == 1){ return 13; }
            else{ return 0; }
        }));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT_SOUL, SoulFireState.NONE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GoldenStoveBlockEntity(pos, state);
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.mirror(mirror);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT_SOUL);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, AllRegistry.Red_nether_brick_stove_be.get(), GoldenStoveBlockEntity::serverTick);
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if(!player.getItemInHand(hand).isEmpty() && level.getBlockEntity(pos) instanceof GoldenStoveBlockEntity entity && !level.getBlockEntity(pos).isRemoved()){
            if(stack.is(Items.FLINT_AND_STEEL)){ entity.kindle(); }
            else if(hitResult.getDirection().equals(Direction.UP)){ entity.items.insertItem(stack, false); }
            else{ entity.fuels.insertItem(stack, false); }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof GoldenStoveBlockEntity entity && !level.getBlockEntity(pos).isRemoved()){
            if(hitResult.getDirection().equals(Direction.UP)){player.setItemInHand(InteractionHand.MAIN_HAND, entity.items.extractItem(false));}
            else{player.setItemInHand(InteractionHand.MAIN_HAND, entity.fuels.extractItem(false));}
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide)
            return;
        if(!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof GoldenStoveBlockEntity entity)
                entity.dropContent();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(AllConstants.LIT_SOUL).equals(SoulFireState.BURN)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,  0.5F + random.nextFloat(),  random.nextFloat() * 0.7F + 0.6F,  false);
            }
            if (random.nextInt(3) == 0) {
                for (int i = 0; i < random.nextInt(2) + 1; i++) {
                    level.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5, (double)pos.getY() + 0.75, (double)pos.getZ() + 0.5,
                            (double)(random.nextFloat() / 2.0F), 5.0E-5, (double)(random.nextFloat() / 2.0F));
                }
            }
        }else if(state.getValue(AllConstants.LIT_SOUL).equals(SoulFireState.SOUL)){
            if (random.nextInt(8) == 0) {
                level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,  0.5F + random.nextFloat(),  random.nextFloat() * 0.7F + 0.6F,  false);
            }
            if (random.nextInt(3) == 0) {
                for (int i = 0; i < random.nextInt(3) + 1; i++) {
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, (double)pos.getX() + 0.5, (double)pos.getY() + 0.75, (double)pos.getZ() + 0.5,
                            (double)(random.nextFloat() / 2.0F), 5.0E-5, (double)(random.nextFloat() / 2.0F));
                }
            }
            if (random.nextInt(3) == 0) {
                level.addParticle(ParticleTypes.SOUL, (double)pos.getX() + 0.5, (double)pos.getY() + 0.75, (double)pos.getZ() + 0.5,
                        (double)(random.nextFloat() / 2.0F), 5.0E-5, (double)(random.nextFloat() / 2.0F));
            }
        }
    }
}
