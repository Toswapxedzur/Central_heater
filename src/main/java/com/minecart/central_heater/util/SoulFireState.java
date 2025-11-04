package com.minecart.central_heater.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

public enum SoulFireState implements StringRepresentable {
    NONE("none", 0),
    BURN("burn", 1),
    SOUL("soul", 2);
    private final String name;
    private final int state;
    SoulFireState(String name, int state){
        this.name = name;
        this.state = state;
    }

    public static final Function<String, SoulFireState> func = StringRepresentable.createNameLookup(values(), s -> s);

    @Override
    public String toString() { return name; }

    @Override
    public String getSerializedName() { return name; }

    public int getState(){ return state; }
}
