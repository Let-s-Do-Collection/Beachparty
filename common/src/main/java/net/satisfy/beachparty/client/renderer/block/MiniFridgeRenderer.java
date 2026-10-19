package net.satisfy.beachparty.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.MiniFridgeBlock;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;

public class MiniFridgeRenderer implements BlockEntityRenderer<MiniFridgeBlockEntity> {
    private static final float SHAKE_ANGLE = 0.3F;
    private static final float SHAKE_OFFSET = 0.002F;
    private static final float SHAKE_SPEED = 2.2F;

    public MiniFridgeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MiniFridgeBlockEntity fridge, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        if (fridge.getLevel() == null) {
            return;
        }
        BlockState state = fridge.getBlockState();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        if (state.getValue(MiniFridgeBlock.FREEZING)) {
            float time = (fridge.getLevel().getGameTime() + partialTick) * SHAKE_SPEED;
            float pulse = 0.6F + 0.4F * Mth.sin(time * 0.11F);
            poseStack.translate(0.5F + Mth.sin(time * 1.7F) * SHAKE_OFFSET * pulse, 0.0F, 0.5F + Mth.cos(time * 1.3F) * SHAKE_OFFSET * pulse);
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(time) * SHAKE_ANGLE * pulse));
            poseStack.translate(-0.5F, 0.0F, -0.5F);
        }
        dispatcher.getModelRenderer().renderModel(poseStack.last(), buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, dispatcher.getBlockModel(state), 1.0F, 1.0F, 1.0F, light, overlay);
        poseStack.popPose();
    }
}
