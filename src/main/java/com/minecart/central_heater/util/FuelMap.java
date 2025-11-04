package com.minecart.central_heater.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;

public class FuelMap {
    public static Map<Item, Integer> soul_fuel_map;

    public static void initializeSoulFuelMap(){
        if(soul_fuel_map != null)
            return;
        soul_fuel_map = new HashMap<>();
        soul_fuel_map.put(Items.SOUL_SAND, 20);
        soul_fuel_map.put(Items.SOUL_SOIL, 10);
        soul_fuel_map.put(Items.SOUL_TORCH, 10);
        soul_fuel_map.put(Items.SOUL_CAMPFIRE, 40);
    }

    public static int getSoulBurnTime(ItemStack stack){
        initializeSoulFuelMap();
        return soul_fuel_map.getOrDefault(stack.getItem(), 0);
    }

    public static int getBurnTime(ItemStack stack){
        if(getSoulBurnTime(stack) == 0)
            return stack.getBurnTime(RecipeType.SMELTING);
        return getSoulBurnTime(stack);
    }
}
