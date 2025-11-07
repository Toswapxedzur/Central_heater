package com.minecart.central_heater.mixin;

import com.minecart.central_heater.util.FireState;
import net.minecraft.world.item.ItemStack;

public interface HopperBlockEntityDuckInterface {
    public FireState getLitState();

    public void setLitState(FireState litState);

    public int getLitTime();

    public void setLitTime(int litTime);

    public ItemStack getFuel(int slot);

    public void setFuel(ItemStack stack, int slot);
}
