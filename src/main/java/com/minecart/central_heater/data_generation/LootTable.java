package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import com.mojang.datafixers.TypeRewriteRule;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.checkerframework.checker.units.qual.A;

import java.util.Set;

public class LootTable extends BlockLootSubProvider {
    public LootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        dropSelf(AllRegistry.stone_brick_tile.get());
        dropSelf(AllRegistry.deepslate_brick_tile.get());
        dropSelf(AllRegistry.stone_stove.get());
        dropSelf(AllRegistry.red_nether_brick_stove.get());
        dropSelf(AllRegistry.brick_stove.get());
        dropSelf(AllRegistry.mud_brick_stove.get());
        dropSelf(AllRegistry.deepslate_stove.get());
        dropSelf(AllRegistry.nether_brick_stove.get());

        dropSelf(AllRegistry.stone_brick_tile_stair.get());
        add(AllRegistry.stone_brick_tile_slab.get(), block -> createSlabItemTable(AllRegistry.stone_brick_tile_slab.get()));
        dropSelf(AllRegistry.stone_brick_tile_wall.get());
        dropSelf(AllRegistry.deepslate_brick_tile_stair.get());
        add(AllRegistry.deepslate_brick_tile_slab.get(), block -> createSlabItemTable(AllRegistry.deepslate_brick_tile_slab.get()));
        dropSelf(AllRegistry.deepslate_brick_tile_wall.get());
        dropSelf(AllRegistry.gold_bars.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return AllRegistry.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

}
