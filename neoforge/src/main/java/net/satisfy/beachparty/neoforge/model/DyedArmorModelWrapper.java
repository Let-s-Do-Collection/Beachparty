package net.satisfy.beachparty.neoforge.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class DyedArmorModelWrapper<T extends LivingEntity> extends EntityModel<T> {
    private final EntityModel<T> original;
    private final int color;

    public DyedArmorModelWrapper(EntityModel<T> original, int color) {
        super(RenderType::entityCutoutNoCull);
        this.original = original;
        this.color = color;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        original.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        original.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
