package com.minecart.central_heater.event;

import com.minecart.central_heater.data_generation.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class DataGeneration {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.EMPTY_SET,
                 List.of(new LootTableProvider.SubProviderEntry(LootTable::new, LootContextParamSets.BLOCK)), lookUpProvider));

        generator.addProvider(event.includeClient(), new BlockState(output, fileHelper));
        generator.addProvider(event.includeClient(), new ItemModel(output, fileHelper));
        generator.addProvider(event.includeClient(), new BlockModel(output, fileHelper));
        generator.addProvider(event.includeServer(), new Recipe(output, lookUpProvider));
        BlockTag blockTag = new BlockTag(output, lookUpProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTag);
        generator.addProvider(event.includeServer(), new ItemTag(output, lookUpProvider, blockTag.contentsGetter()));
    }
}
