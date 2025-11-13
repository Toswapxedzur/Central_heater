package com.minecart.central_heater.util;

import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.HashMap;
import java.util.Map;

public class FuelMap {
    public static final Map<Item, Integer> soul_fuel_map = new HashMap<>();

    public static void initializeSoulFuelMap(){
        soul_fuel_map.put(Items.SOUL_SAND, 400);
        soul_fuel_map.put(Items.SOUL_SOIL, 200);
    }

    public static int getSoulBurnTime(ItemStack stack){
        return soul_fuel_map.getOrDefault(stack.getItem(), 0);
    }

    public static int getBurnTime(ItemStack stack){
        if(getSoulBurnTime(stack) == 0)
            return stack.getBurnTime(RecipeType.SMELTING);
        return getSoulBurnTime(stack);
    }
}
