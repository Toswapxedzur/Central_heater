package com.minecart.central_heater.event;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@EventBusSubscriber
public class BlockPlacementHelper {
    @SubscribeEvent
    public static void onUseItemOnBlock(UseItemOnBlockEvent event){
        if(event.getLevel().isClientSide)
            return;
        if(event.getUsePhase() == UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK){
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState block = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Entity player = event.getPlayer();

            Direction blockDirection = Direction.NORTH;
            if(block.hasProperty(BlockStateProperties.HORIZONTAL_FACING)){
                blockDirection = block.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }else if(block.hasProperty(BlockStateProperties.FACING) && block.getValue(BlockStateProperties.FACING).getAxis().isHorizontal()){
                blockDirection = block.getValue(BlockStateProperties.FACING);
            }else if(block.hasProperty(BlockStateProperties.FACING_HOPPER) && block.getValue(BlockStateProperties.FACING_HOPPER).getAxis().isHorizontal()){
                blockDirection = block.getValue(BlockStateProperties.FACING_HOPPER);
            }

            if(player!= null) {
                if (blockEntity instanceof RandomizableContainerBlockEntity && event.getItemStack().is(AllRegistry.Blast_overheater_item)) {
                    if(!level.getBlockState(pos.above()).isAir())
                        return;
                    boolean flag=true;
                    for(BlockPos i : BlockPos.betweenClosed(pos.offset(-1, 1, -1), pos.offset(1, 1, 1))){
                        if(level.getBlockState(i).is(AllRegistry.Blast_overheater.get()))
                            flag = false;
                    }
                    if(!flag)
                        return;
                    ItemStack stack = event.getItemStack();
                    event.getLevel().setBlock(pos.above(), AllRegistry.Blast_overheater.get().
                            defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockDirection), 2);
                    stack.shrink(1);
                    event.getPlayer().awardStat(Stats.ITEM_USED.get(AllRegistry.Blast_overheater_item.asItem()));
                    event.cancelWithResult(ItemInteractionResult.SUCCESS);
                }
            }
        }
    }
}
