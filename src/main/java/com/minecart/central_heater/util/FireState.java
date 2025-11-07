package com.minecart.central_heater.util;

import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

public enum FireState implements StringRepresentable {
    NONE("none", false),
    LIT("lit", true);
    private final String name;
    private final boolean state;
    FireState(String name, boolean state){
        this.name = name;
        this.state  = state;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static final Function<String, FireState> func = StringRepresentable.createNameLookup(values(), i -> i);

    public static FireState getFireState(boolean state){
        return state ? FireState.LIT : FireState.NONE;
    }

    public boolean getBooleanState(){
        return this.state;
    }
}
