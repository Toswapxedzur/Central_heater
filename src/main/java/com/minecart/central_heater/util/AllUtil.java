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
            event.accept(AllRegistry.stone_stove.asItem());
            event.accept(AllRegistry.red_nether_brick_stove.asItem());
            event.accept(AllRegistry.brick_stove.asItem());
            event.accept(AllRegistry.mud_brick_stove.asItem());
            event.accept(AllRegistry.deepslate_stove.asItem());
            event.accept(AllRegistry.nether_brick_stove.asItem());
        }else if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(AllRegistry.stone_brick_tile.asItem());
            event.accept(AllRegistry.deepslate_brick_tile.asItem());
            event.accept(AllRegistry.stone_brick_tile_stair.asItem());
            event.accept(AllRegistry.stone_brick_tile_slab.asItem());
            event.accept(AllRegistry.stone_brick_tile_wall.asItem());
            event.accept(AllRegistry.deepslate_brick_tile_stair.asItem());
            event.accept(AllRegistry.deepslate_brick_tile_slab.asItem());
            event.accept(AllRegistry.deepslate_brick_tile_wall.asItem());
            event.accept(AllRegistry.gold_bars.asItem());
        }else if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(AllRegistry.cobble.asItem());
            event.accept(AllRegistry.deepslate_cobble.asItem());
            event.accept(AllRegistry.stone_brick.asItem());
            event.accept(AllRegistry.mud_brick.asItem());
            event.accept(AllRegistry.red_nether_brick.asItem());
            event.accept(AllRegistry.deepslate_brick.asItem());
            event.accept(AllRegistry.diamond_shard.asItem());
        }
    }

    public static boolean isFlatItem(ItemStack stack){
        return !Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack).isGui3d();
    }

    public static boolean isFlatItem(Item item){
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item).isGui3d();
    }

    public static int itemSizeNumberFunction(ItemStack stack){
        if(isFlatItem(stack)){
            return 6;
        }
        return 1;
    }
}
