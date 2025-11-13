package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ItemTag extends ItemTagsProvider {
    public ItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.BRICKS).add(AllRegistry.mud_brick.asItem(), AllRegistry.stone_brick.asItem(), AllRegistry.deepslate_brick.asItem(), AllRegistry.red_nether_brick.asItem());
        tag(Tags.Items.NUGGETS).add(AllRegistry.diamond_shard.asItem());
    }
}
