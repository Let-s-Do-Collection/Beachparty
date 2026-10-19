package net.satisfy.beachparty.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.client.util.IngredientColors;
import net.satisfy.beachparty.core.block.CocktailBlock;
import net.satisfy.beachparty.core.block.PalmBarBlock;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;

import java.util.List;

public class PalmBarRenderer implements BlockEntityRenderer<PalmBarBlockEntity> {
    private static final float HOP_TICKS = PalmBarBlockEntity.SHAKE_COOLDOWN;
    private static final float HOP_HEIGHT = 0.14F;
    private static final float HOP_ANGLE = 14.0F;
    private static final float SLOSH_HEIGHT = 1.4F;
    private static final int LAYER_ALPHA = 230;

    private final CocktailRenderer cocktails;

    public PalmBarRenderer(BlockEntityRendererProvider.Context context) {
        this.cocktails = new CocktailRenderer(context);
    }

    @Override
    public void render(PalmBarBlockEntity bar, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        Level level = bar.getLevel();
        if (level == null) {
            return;
        }
        ItemStack result = bar.getResult();
        List<ItemStack> ingredients = bar.getIngredients();
        if (result.isEmpty() && ingredients.isEmpty() || !PalmBarBlock.hasRoom(level, bar.getBlockPos())) {
            return;
        }
        PalmBarRecipe preview = bar.getPreview();
        Block glass = bar.getGlassBlock();
        if (glass != null && !cocktails.hasLiquid(glass)) {
            glass = null;
        }
        Direction facing = bar.getBlockState().getValue(PalmBarBlock.FACING);
        int topLight = LevelRenderer.getLightColor(level, bar.getBlockPos().above());

        float age = level.getGameTime() + partialTick - bar.getLastShake();
        float hop = age >= 0.0F && age < HOP_TICKS ? age / HOP_TICKS : 1.0F;
        float strength = 1.0F - hop;
        float tiltX = SLOSH_HEIGHT * strength * Mth.sin(age * 1.6F);
        float tiltZ = SLOSH_HEIGHT * 0.5F * strength * Mth.cos(age * 2.1F);

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.0F + Mth.sin(hop * Mth.PI) * HOP_HEIGHT, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(hop * Mth.PI * 3.0F) * HOP_ANGLE * strength));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(hop * Mth.PI * 2.0F) * HOP_ANGLE * 0.4F * strength * Mth.sin(hop * Mth.PI)));
        poseStack.translate(-0.5F, 0.0F, -0.5F);

        if (glass == null) {
            renderItems(result.isEmpty() ? ingredients : List.of(result), poseStack, buffers, level, topLight, overlay);
            poseStack.popPose();
            return;
        }
        BlockState state = glass.defaultBlockState().setValue(CocktailBlock.FACING, facing).setValue(CocktailBlock.STAGE, result.isEmpty() ? 0 : 3);
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        dispatcher.getModelRenderer().renderModel(poseStack.last(), buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, dispatcher.getBlockModel(state), 1.0F, 1.0F, 1.0F, topLight, overlay);
        if (!result.isEmpty()) {
            cocktails.renderFull(state, poseStack, buffers, tiltX, tiltZ, topLight, overlay);
        } else if (preview != null) {
            renderLayers(bar, ingredients, hop, state, poseStack, buffers, tiltX, tiltZ, topLight, overlay);
        }
        poseStack.popPose();
    }

    private void renderLayers(PalmBarBlockEntity bar, List<ItemStack> ingredients, float hop, BlockState state, PoseStack poseStack, MultiBufferSource buffers, float tiltX, float tiltZ, int light, int overlay) {
        int shakes = bar.getShakes();
        float eased = 1.0F - (1.0F - hop) * (1.0F - hop);
        float mix = shakes == 0 ? 0.0F : (shakes - 1 + eased) / PalmBarBlockEntity.SHAKES;
        float thickness = Math.max(1.0F, Mth.lerp(mix, 1.0F, cocktails.fillHeight(state.getBlock()) / ingredients.size()));
        int mixed = IngredientColors.mix(ingredients);
        for (int i = 0; i < ingredients.size(); i++) {
            int color = blend(IngredientColors.get(ingredients.get(i)), mixed, mix);
            boolean top = i == ingredients.size() - 1;
            cocktails.renderLayer(state, i * thickness, (i + 1) * thickness, color, poseStack, buffers, top ? tiltX : 0.0F, top ? tiltZ : 0.0F, light, overlay);
        }
    }

    private static int blend(int from, int to, float amount) {
        return FastColor.ARGB32.color(LAYER_ALPHA,
                (int) Mth.lerp(amount, FastColor.ARGB32.red(from), FastColor.ARGB32.red(to)),
                (int) Mth.lerp(amount, FastColor.ARGB32.green(from), FastColor.ARGB32.green(to)),
                (int) Mth.lerp(amount, FastColor.ARGB32.blue(from), FastColor.ARGB32.blue(to)));
    }

    private void renderItems(List<ItemStack> stacks, PoseStack poseStack, MultiBufferSource buffers, Level level, int light, int overlay) {
        for (int i = 0; i < stacks.size(); i++) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.15F + i * 0.06F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(i * 37.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.4F, 0.4F, 0.4F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stacks.get(i), ItemDisplayContext.FIXED, light, overlay, poseStack, buffers, level, 0);
            poseStack.popPose();
        }
    }
}
