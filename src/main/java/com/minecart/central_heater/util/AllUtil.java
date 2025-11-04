package com.minecart.central_heater.util;

import com.minecart.central_heater.AllRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class AllUtil {
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(AllRegistry.Blast_overheater_item.asItem());
            event.accept(AllRegistry.Stone_stove_item.asItem());
            event.accept(AllRegistry.Red_nether_brick_stove_item.asItem());
        }else if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(AllRegistry.Stone_tile_item.asItem());
        }
    }

    public static boolean isFlatItem(ItemStack stack){
        return !Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack).isGui3d();
    }

    public static boolean isFlatItem(Item item){
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item).isGui3d();
    }

    public static Integer itemSizeNumberFunction(ItemStack stack){
        if(isFlatItem(stack)){
            return 6;
        }
        return 1;
    }
}
