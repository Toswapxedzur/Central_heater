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
        simpleBlockWithItem(AllRegistry.Stone_tile.get(), cubeAll(AllRegistry.Stone_tile.get()));
    }
}
