package com.minecart.central_heater.block_entity_renderer;

import com.minecart.central_heater.block_entity.BurnableCampfireBlockEntity;
import com.minecart.central_heater.util.AllUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class BurnableCampfireBlockEntityRenderer implements BlockEntityRenderer<BurnableCampfireBlockEntity> {

    private final ItemRenderer itemRenderer;
    private final CampfireRenderer parentRenderer;
    public BurnableCampfireBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        this.parentRenderer = new CampfireRenderer(context);
    }

    @Override
    public void render(BurnableCampfireBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(CampfireBlock.FACING);
        NonNullList<ItemStack> nonnulllist = blockEntity.getFuels();

        this.parentRenderer.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        for (int j = 0; j < nonnulllist.size(); j++) {
            ItemStack itemstack = nonnulllist.get(j);
            if (itemstack != ItemStack.EMPTY) {
                poseStack.pushPose();
                poseStack.translate(0.5f , (0.28f + j * 0.25f), 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
                if(AllUtil.isFlatItem(itemstack)){
                    poseStack.translate(0, -0.2f, 0f);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    poseStack.scale(0.8f, 0.8f, 0.8f);
                }
                poseStack.scale(0.5f, 0.5f, 0.5f);
                itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), j);
                poseStack.popPose();
            }
        }
    }
}
