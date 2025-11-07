package com.minecart.central_heater.util;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

public class AllConstants {
    public static final EnumProperty<SoulFireState> LIT_SOUL = EnumProperty.create("lit_soul", SoulFireState.class);

    public static final Vec3[] topInvLoc1 = new Vec3[]{
            new Vec3(0.5, 1.12, 0.5)};

    public static final Vec3[] topInvLoc4 = new Vec3[]{
            new Vec3(0.25, 1.12, 0.25), new Vec3(0.25, 1.12, -0.25),
            new Vec3(-0.25, 1.12, 0.25), new Vec3(-0.25, 1.12, -0.25)};

    public static final Vec3[] topInvLoc9 = new Vec3[]{
            new Vec3(0.3, 1.12, 0.3), new Vec3(0.3, 1.12, 0), new Vec3(0.3, 1.12, -0.3),
            new Vec3(0, 1.12, 0.3), new Vec3(0, 1.12, 0), new Vec3(0, 1.12, -0.3),
            new Vec3(-0.3, 1.12, 0.3), new Vec3(-0.3, 1.12, 0), new Vec3(-0.3, 1.12, -0.3)};

    public static final Vec3[][] fuelInvLoc4 = new Vec3[][]{
            {new Vec3(0, 0.3, 0)},
            {new Vec3(-0.15, 0.3, 0), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0), new Vec3(0, 0.55, 0)}};

}
