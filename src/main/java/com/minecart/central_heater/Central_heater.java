package com.minecart.central_heater;

import com.minecart.central_heater.block_entity_renderer.BrickStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.GoldenStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.StoneStoveBlockEntityRenderer;
import com.minecart.central_heater.util.AllUtil;
import com.minecart.central_heater.util.FuelMap;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;


@Mod(Central_heater.MODID)
public class Central_heater {

    public static final String MODID = "central_heater";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Central_heater(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        AllRegistry.register(modEventBus);

        modEventBus.addListener(AllUtil::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        FuelMap.initializeSoulFuelMap();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(AllRegistry.Stone_stove_be.get(), StoneStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllRegistry.Red_nether_brick_stove_be.get(), GoldenStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllRegistry.brick_stove_be.get(), BrickStoveBlockEntityRenderer::new);
        }
    }
}
