package net.satisfy.beachparty.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.TrunksModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class TrunksLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final TrunksModel<T> model;

    public TrunksLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new TrunksModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TrunksModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack[] trunks = new ItemStack[]{ItemStack.EMPTY};
        boolean[] inCurioSlot = new boolean[]{false};

        top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(entity).ifPresent(curios -> {
            if (curios.isEquipped(ObjectRegistry.TRUNKS.get())) {
                curios.findFirstCurio(stack -> stack.is(ObjectRegistry.TRUNKS.get()))
                        .ifPresent(curio -> {
                            inCurioSlot[0] = true;
                            trunks[0] = curio.stack();
                        });
            }
        });

        boolean inLegsSlot = entity.getItemBySlot(EquipmentSlot.LEGS).is(ObjectRegistry.TRUNKS.get());
        if (inLegsSlot) {
            trunks[0] = entity.getItemBySlot(EquipmentSlot.LEGS);
        }

        if (!inCurioSlot[0] && !inLegsSlot) return;

        if (trunks[0].hasTag()) {
            assert trunks[0].getTag() != null;
            if (trunks[0].getTag().contains("Visible") && !trunks[0].getTag().getBoolean("Visible")) {
                return;
            }
        }

        DyeableBeachpartyArmorItem item = trunks[0].getItem() instanceof DyeableBeachpartyArmorItem
                ? (DyeableBeachpartyArmorItem) trunks[0].getItem() : null;
        if (item == null) return;

        int colorInt = item.getColor(trunks[0]);
        float red = ((colorInt >> 16) & 0xFF) / 255f;
        float green = ((colorInt >> 8) & 0xFF) / 255f;
        float blue = (colorInt & 0xFF) / 255f;

        poseStack.pushPose();
        renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, red, green, blue);
        poseStack.popPose();
    }


    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return new BeachpartyIdentifier("textures/models/armor/trunks.png");
    }
}
