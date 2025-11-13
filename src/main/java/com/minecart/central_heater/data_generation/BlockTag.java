package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.Central_heater;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ibm.icu.util.LocalePriorityList.add;

public class BlockTag extends BlockTagsProvider {
    public BlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Central_heater.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllRegistry.brick_stove.get(), AllRegistry.mud_brick_stove.get(), AllRegistry.stone_stove.get()
                , AllRegistry.deepslate_stove.get(), AllRegistry.nether_brick_stove.get(), AllRegistry.red_nether_brick_stove.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllRegistry.stone_brick_tile.get(), AllRegistry.stone_brick_tile_stair.get(), AllRegistry.stone_brick_tile_slab.get(),
                 AllRegistry.stone_brick_tile_wall.get(), AllRegistry.deepslate_brick_tile.get(), AllRegistry.deepslate_brick_tile_stair.get(), AllRegistry.deepslate_brick_tile_slab.get(),
                 AllRegistry.gold_bars.get());

        tag(BlockTags.NEEDS_STONE_TOOL).add(AllRegistry.nether_brick_stove.get(), AllRegistry.red_nether_brick_stove.get());

        tag(BlockTags.NEEDS_STONE_TOOL).add(AllRegistry.gold_bars.get());

        tag(BlockTags.STAIRS).add(AllRegistry.stone_brick_tile_stair.value(), AllRegistry.deepslate_brick_tile_stair.get());
        tag(BlockTags.SLABS).add(AllRegistry.stone_brick_tile_slab.value(), AllRegistry.deepslate_brick_tile_slab.get());
        tag(BlockTags.WALLS).add(AllRegistry.stone_brick_tile_wall.value(), AllRegistry.deepslate_brick_tile_wall.get());
    }
}
