package com.minecart.central_heater.event;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.block.BlastOverheaterBlock;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.block_entity.BlastOverheaterBlockEntity;
import com.minecart.central_heater.block_entity.GoldenStoveBlockEntity;
import com.minecart.central_heater.block_entity.StoneStoveBlockEntity;
import com.minecart.central_heater.mixin.HopperBlockEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class Event {

    @SubscribeEvent
    public static void rightClick(PlayerInteractEvent.RightClickBlock event){
        System.out.println();
    }
}
