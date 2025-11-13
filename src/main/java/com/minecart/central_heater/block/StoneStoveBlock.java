package com.minecart.central_heater.block;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block_entity.BrickStoveBlockEntity;
import com.minecart.central_heater.block_entity.StoneStoveBlockEntity;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoneStoveBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    public static final MapCodec<StoneStoveBlock> CODEC = simpleCodec(StoneStoveBlock::new);


    public StoneStoveBlock(Properties properties){
        super(properties.noOcclusion().lightLevel(state -> state.getValue(LIT) ? 13 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoneStoveBlockEntity(pos, state);
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
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }


    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllRegistry.stone_stove_be.get(), StoneStoveBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, AllRegistry.stone_stove_be.get(), StoneStoveBlockEntity::serverTick);
        }
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if(!player.getItemInHand(hand).isEmpty() && !(level.getBlockEntity(pos) == null) && !level.getBlockEntity(pos).isRemoved() && level.getBlockEntity(pos) instanceof StoneStoveBlockEntity entity){
            if(stack.is(Items.FLINT_AND_STEEL)){ entity.kindle(); }
            else if(hitResult.getDirection().equals(Direction.UP)){ entity.items.insertItem(stack, false); }
            else { entity.fuels.insertItem(stack, false); }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) != null && !level.getBlockEntity(pos).isRemoved() && level.getBlockEntity(pos) instanceof StoneStoveBlockEntity entity){
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
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof StoneStoveBlockEntity entity)
                entity.dropContent();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return !state.getValue(LIT).booleanValue();
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos) &&
                level.getBlockEntity(hit.getBlockPos()) instanceof StoneStoveBlockEntity entity && !entity.isLit()) {
            entity.kindle();
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().campfire(), (float)2.0f);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5;
            double d1 = (double)pos.getY() + 0.75;
            double d2 = (double)pos.getZ() + 0.5;

            if (random.nextInt(10) == 0) {
                level.playLocalSound(d0, d1, d2, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                        0.5F + random.nextFloat(), random.nextFloat() * 0.8F + 1F, false);
            }

            for (int i = 0; i < random.nextInt(1) + 1; i++) {
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, (random.nextFloat() / 2.0F), 5.0E-5, (random.nextFloat() / 2.0F));
            }

            for(int i=0;i<random.nextIntBetweenInclusive(4,6);i++){
                level.addAlwaysVisibleParticle(ParticleTypes.SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                         0.0, 0.07, 0.0);
            }

            for(int i=0;i<random.nextIntBetweenInclusive(2,3);i++){
                level.addParticle(ParticleTypes.WHITE_SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }

            for(int i=0;i<random.nextIntBetweenInclusive(1,3);i++){
                level.addParticle(ParticleTypes.LARGE_SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }

            for(int i=0;i<random.nextIntBetweenInclusive(1,2);i++){
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }
    }
}
