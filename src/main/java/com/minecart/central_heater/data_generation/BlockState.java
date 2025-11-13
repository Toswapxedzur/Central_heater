package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.Central_heater;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockState extends BlockStateProvider {
    public BlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Central_heater.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(AllRegistry.stone_brick_tile.get(), cubeAll(AllRegistry.stone_brick_tile.get()));
        simpleBlockWithItem(AllRegistry.deepslate_brick_tile.get(), cubeAll(AllRegistry.deepslate_brick_tile.get()));

        stairsBlock(AllRegistry.stone_brick_tile_stair.get(), blockTexture(AllRegistry.stone_brick_tile.get()));
        slabBlock(AllRegistry.stone_brick_tile_slab.get(), blockTexture(AllRegistry.stone_brick_tile.get()), blockTexture(AllRegistry.stone_brick_tile.get()));
        wallBlock(AllRegistry.stone_brick_tile_wall.get(), blockTexture(AllRegistry.stone_brick_tile.get()));

        stairsBlock(AllRegistry.deepslate_brick_tile_stair.get(), blockTexture(AllRegistry.deepslate_brick_tile.get()));
        slabBlock(AllRegistry.deepslate_brick_tile_slab.get(), blockTexture(AllRegistry.deepslate_brick_tile.get()), blockTexture(AllRegistry.deepslate_brick_tile.get()));
        wallBlock(AllRegistry.deepslate_brick_tile_wall.get(), blockTexture(AllRegistry.deepslate_brick_tile.get()));
    }
}
