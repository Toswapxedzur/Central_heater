package com.minecart.central_heater;

import com.minecart.central_heater.block.BlastOverheaterBlock;
import com.minecart.central_heater.block_entity.BlastOverheaterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

public class AllRegistry {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Central_heater.MODID);
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Central_heater.MODID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Central_heater.MODID);

    public static DeferredBlock<Block> Blast_overheater = BLOCKS.register("blast_overheater", ()->new BlastOverheaterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE).noOcclusion()));
    public static DeferredItem<BlockItem> Blast_overheater_item = ITEMS.registerSimpleBlockItem(Blast_overheater);
    public static Supplier<BlockEntityType<BlastOverheaterBlockEntity>> Blast_overheater_be = BLOCK_ENTITIES.register("blast_overheater",
            ()->BlockEntityType.Builder.of(BlastOverheaterBlockEntity::new, Blast_overheater.get()).build(null));

    public static void register(IEventBus modEventbus){
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        BLOCK_ENTITIES.register(modEventbus);
    }
}
