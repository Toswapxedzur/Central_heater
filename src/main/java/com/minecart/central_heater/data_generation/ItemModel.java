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
    }
}
