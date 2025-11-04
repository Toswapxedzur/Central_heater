package com.minecart.central_heater.block_entity_renderer;

import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.block_entity.StoneStoveBlockEntity;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.AllUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StoneStoveBlockEntityRenderer implements BlockEntityRenderer<StoneStoveBlockEntity> {
    public final BlockEntityRendererProvider.Context context;
    private static final Vec3[][] fuelLoc = AllConstants.fuelInvLoc4;
    private static final Vec3[] invLoc = AllConstants.topInvLoc4;

    public StoneStoveBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }
    @Override
    public void render(StoneStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderInv(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        renderFuel(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public void renderInv(StoneStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int j=blockEntity.items.countStack();
        for(int i=0;i<j;i++){
            ItemStack stack = blockEntity.items.getStackInSlot(i);
            poseStack.pushPose();
            poseStack.translate(invLoc[i].x, invLoc[i].y, invLoc[i].z);
            if(AllUtil.isFlatItem(stack)){
                poseStack.scale(0.4f, 0.4f, 0.4f);
                poseStack.translate(0, -0.25f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }else{
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(StoneStoveBlock.FACING).toYRot()));
            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
                    packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    public void renderFuel(StoneStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int j = blockEntity.fuels.countStack()-1;
        if(j<0)
            return;
        boolean[] blocky = new boolean[4];
        for(int i = 0; i<blockEntity.fuels.countStack(); i++){
            ItemStack stack = blockEntity.fuels.getStackInSlot(i);
            blocky[i] = getRenderer().getItemModelShaper().getItemModel(stack).isGui3d();

            poseStack.pushPose();
            poseStack.translate(fuelLoc[j][i].x, fuelLoc[j][i].y, fuelLoc[j][i].z);
            if(i==3 && !blocky[0] && !blocky[1] && !blocky[2])
                poseStack.translate(0, -0.2f, 0);
            if(!blocky[i]) {
                poseStack.scale(0.4f, 0.4f, 0.4f);
                poseStack.translate(0, -0.25f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }else{
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(StoneStoveBlock.FACING).toYRot()));
            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
                    packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    public ItemRenderer getRenderer(){
        return context.getItemRenderer();
    }
}
