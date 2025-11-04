package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Stream;

public class LootTable extends BlockLootSubProvider {
    public LootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        dropSelf(AllRegistry.Blast_overheater.get());
        dropSelf(AllRegistry.Stone_tile.get());
        dropSelf(AllRegistry.Stone_stove.get());
        dropSelf(AllRegistry.Red_nether_brick_stove.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return AllRegistry.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

}
