package com.minecart.central_heater.util;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

public class AllConstants {
    public static final EnumProperty<SoulFireState> LIT_SOUL = EnumProperty.create("lit_soul", SoulFireState.class);

    public static final Vec3[] topInvLoc4 = new Vec3[]{
            new Vec3(0.7, 1.12, 0.7), new Vec3(0.7, 1.12, 0.3),
            new Vec3(0.3, 1.12, 0.7), new Vec3(0.3, 1.12, 0.3)};

    public static final Vec3[] topInvLoc9 = new Vec3[]{
            new Vec3(0.8, 1.12, 0.8), new Vec3(0.8, 1.12, 0.5), new Vec3(0.8, 1.12, 0.2),
            new Vec3(0.5, 1.12, 0.8), new Vec3(0.5, 1.12, 0.5), new Vec3(0.5, 1.12, 0.2),
            new Vec3(0.2, 1.12, 0.8), new Vec3(0.2, 1.12, 0.5), new Vec3(0.2, 1.12, 0.2)};

    public static final Vec3[][] fuelInvLoc4 = new Vec3[][]{
            {new Vec3(0.5, 0.3, 0.5)},
            {new Vec3(0.35, 0.3, 0.5), new Vec3(0.65, 0.3, 0.5)},
            {new Vec3(0.35, 0.3, 0.35), new Vec3(0.35, 0.3, 0.65), new Vec3(0.65, 0.3, 0.5)},
            {new Vec3(0.35, 0.3, 0.35), new Vec3(0.35, 0.3, 0.65), new Vec3(0.65, 0.3, 0.5), new Vec3(0.5, 0.55, 0.5)}};
}
