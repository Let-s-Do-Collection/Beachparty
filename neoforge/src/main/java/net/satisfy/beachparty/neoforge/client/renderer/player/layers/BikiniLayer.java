package net.satisfy.beachparty.neoforge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.beachparty.client.model.BikiniModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BikiniLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final BikiniModel<T> model;

    public BikiniLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new BikiniModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BikiniModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof Player) {
            ItemStack legsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (legsStack.isEmpty() || !(legsStack.getItem() instanceof DyeableBeachpartyArmorItem)) return;
            if (legsStack.has(DataComponents.CUSTOM_DATA)) {
                if (legsStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("Visible") && !legsStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("Visible")) return;
            }
            int colorInt = Objects.requireNonNull(legsStack.get(DataComponents.DYED_COLOR)).rgb();
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, light, entity, colorInt);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return BeachpartyIdentifier.identifier("textures/models/armor/bikini.png");
    }
}
