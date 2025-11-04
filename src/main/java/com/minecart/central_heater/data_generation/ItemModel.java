package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.Central_heater;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModel extends ItemModelProvider {
    public ItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Central_heater.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("blast_overheater", modLoc("block/blast_overheater_off"));
        withExistingParent("stone_stove", modLoc("block/stone_stove_off"));
        withExistingParent("red_nether_brick_stove", modLoc("block/red_nether_brick_stove_off"));
        basicItem(AllRegistry.Cobble.asItem());
        basicItem(AllRegistry.Stone_brick.asItem());
    }
}
