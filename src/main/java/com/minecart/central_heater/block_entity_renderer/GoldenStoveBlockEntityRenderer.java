package com.minecart.central_heater.block_entity_renderer;

import com.minecart.central_heater.block_entity.GoldenStoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GoldenStoveBlockEntityRenderer extends AbstractStoveBlockEntityRenderer implements BlockEntityRenderer<GoldenStoveBlockEntity> {

    public GoldenStoveBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 4, 9);
    }

    @Override
    public void render(GoldenStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
