package net.satisfy.beachparty.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.CocktailBlock;
import net.satisfy.beachparty.core.block.entity.CocktailBlockEntity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

public class CocktailRenderer implements BlockEntityRenderer<CocktailBlockEntity> {
    private static final float DRAIN_TICKS = 12.0F;
    private static final float SLOSH_TICKS = 40.0F;
    private static final float SLOSH_HEIGHT = 0.7F;
    private static final float SLOSH_SPEED = 0.8F;
    private static final float EMPTY = 0.02F;
    private static final ResourceLocation LAYER_TEXTURE = ResourceLocation.withDefaultNamespace("block/white_concrete");

    private final Map<Block, Liquid> liquids = new HashMap<>();

    public CocktailRenderer(BlockEntityRendererProvider.Context context) {
        ResourceLocation sweetberries = texture("sweetberries_cocktail_1");
        liquids.put(SWEETBERRIES_COCKTAIL.get(), new Liquid(sweetberries, sweetberries, new float[]{0.25F, 3.25F, 7.01F, 10.01F}, List.of(
                new Segment(6.25F, 6.25F, 9.25F, 9.25F, 0.25F, 5.25F, new float[]{2, 6, 5, 11}, new float[]{2, 3, 5, 6}),
                new Segment(5.5F, 5.25F, 10.5F, 10.25F, 5.25F, 10.01F, new float[]{1, 2, 6, 7}, new float[]{6, 2, 11, 7}))));
        ResourceLocation cocoa = texture("cocoa_cocktail");
        liquids.put(COCOA_COCKTAIL.get(), new Liquid(cocoa, cocoa, new float[]{6.05F, 7.05F, 9.05F, 11.05F}, List.of(
                new Segment(5.5F, 5.5F, 10.5F, 10.5F, 6.05F, 11.05F, new float[]{6, 6, 11, 11}, new float[]{6, 6, 11, 11}))));
        ResourceLocation melon = texture("melon_cocktail");
        liquids.put(MELON_COCKTAIL.get(), new Liquid(melon, melon, new float[]{6.05F, 8.05F, 10.05F, 11.05F}, List.of(
                new Segment(5.5F, 5.5F, 10.5F, 10.5F, 6.05F, 11.05F, new float[]{6, 6, 11, 11}, new float[]{6, 6, 11, 11}))));
        ResourceLocation honey = texture("honey_cocktail_1");
        liquids.put(HONEY_COCKTAIL.get(), new Liquid(honey, honey, new float[]{4.25F, 6.75F, 8.75F, 10.75F}, List.of(
                new Segment(5.25F, 5.25F, 10.75F, 10.75F, 4.25F, 10.75F, new float[]{0, 5, 6, 12}, new float[]{6, 5, 12, 11}))));
        liquids.put(PUMPKIN_COCKTAIL.get(), new Liquid(texture("pumpkin_cocktail_1"), texture("pumpkin_cocktail_0"), new float[]{0.05F, 3.05F, 5.05F, 7.05F}, List.of(
                new Segment(5.0F, 5.0F, 11.0F, 11.0F, 0.05F, 7.05F, new float[]{3, 5, 9, 12}, new float[]{0, 10, 6, 16}))));
        ResourceLocation coconut = texture("coconut_cocktail");
        liquids.put(COCONUT_COCKTAIL.get(), new Liquid(coconut, coconut, new float[]{5.0F, 5.5F, 6.0F, 7.0F}, List.of(
                new Segment(5.0F, 5.0F, 11.0F, 11.0F, 5.0F, 7.0F, new float[]{0, 0, 6, 2}, new float[]{0, 0, 6, 6}))));
    }

    private static ResourceLocation texture(String name) {
        return BeachpartyIdentifier.identifier("block/" + name);
    }

    @Override
    public void render(CocktailBlockEntity cocktail, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        Level level = cocktail.getLevel();
        BlockState state = cocktail.getBlockState();
        Liquid liquid = liquids.get(state.getBlock());
        if (level == null || liquid == null) {
            return;
        }
        int stage = state.getValue(CocktailBlock.STAGE);
        float time = level.getGameTime() + partialTick;
        if (cocktail.getShownStage() < 0) {
            cocktail.show(stage, liquid.levels()[stage], time - SLOSH_TICKS);
        } else if (cocktail.getShownStage() != stage) {
            cocktail.show(stage, surface(cocktail, liquid, time), time);
        }
        float surface = surface(cocktail, liquid, time);
        if (surface <= liquid.segments().getFirst().y0() + EMPTY) {
            return;
        }
        float age = time - cocktail.getChangeTime();
        float fade = Math.max(0.0F, 1.0F - age / SLOSH_TICKS);
        float wave = SLOSH_HEIGHT * fade * fade;
        float tiltX = wave * Mth.sin(age * SLOSH_SPEED);
        float tiltZ = wave * 0.5F * Mth.cos(age * SLOSH_SPEED * 1.3F);
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        poseStack.pushPose();
        rotate(poseStack, state);
        renderVolume(buffers.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS)), poseStack.last(), liquid.segments(), liquid.segments().getFirst().y0(), surface, atlas.apply(liquid.side()), atlas.apply(liquid.top()), -1, tiltX, tiltZ, light, overlay);
        poseStack.popPose();
    }

    public float fillHeight(Block block) {
        Liquid liquid = liquids.get(block);
        return liquid == null ? 0.0F : liquid.levels()[3] - liquid.segments().getFirst().y0();
    }

    public boolean hasLiquid(Block block) {
        return liquids.containsKey(block);
    }

    public void renderFull(BlockState state, PoseStack poseStack, MultiBufferSource buffers, float tiltX, float tiltZ, int light, int overlay) {
        Liquid liquid = liquids.get(state.getBlock());
        if (liquid == null) {
            return;
        }
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        poseStack.pushPose();
        rotate(poseStack, state);
        renderVolume(buffers.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS)), poseStack.last(), liquid.segments(), liquid.segments().getFirst().y0(), liquid.levels()[3], atlas.apply(liquid.side()), atlas.apply(liquid.top()), -1, tiltX, tiltZ, light, overlay);
        poseStack.popPose();
    }

    public void renderLayer(BlockState state, float from, float to, int color, PoseStack poseStack, MultiBufferSource buffers, float tiltX, float tiltZ, int light, int overlay) {
        Liquid liquid = liquids.get(state.getBlock());
        if (liquid == null) {
            return;
        }
        float base = liquid.segments().getFirst().y0();
        float bottom = base + from;
        float top = Math.min(base + to, liquid.levels()[3]);
        if (top <= bottom) {
            return;
        }
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(LAYER_TEXTURE);
        poseStack.pushPose();
        rotate(poseStack, state);
        renderVolume(buffers.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS)), poseStack.last(), liquid.segments(), bottom, top, sprite, sprite, color, tiltX, tiltZ, light, overlay);
        poseStack.popPose();
    }

    private static void rotate(PoseStack poseStack, BlockState state) {
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-(state.getValue(CocktailBlock.FACING).toYRot() + 180.0F)));
        poseStack.translate(-0.5F, 0.0F, -0.5F);
    }

    private static void renderVolume(VertexConsumer consumer, PoseStack.Pose pose, List<Segment> segments, float bottom, float surface, TextureAtlasSprite side, TextureAtlasSprite top, int color, float tiltX, float tiltZ, int light, int overlay) {
        for (int i = 0; i < segments.size(); i++) {
            Segment full = segments.get(i);
            if (full.y1() <= bottom) {
                continue;
            }
            Segment segment = bottom > full.y0() ? new Segment(full.x0(), full.z0(), full.x1(), full.z1(), bottom, full.y1(), full.sideUv(), full.topUv()) : full;
            if (surface <= segment.y0() + EMPTY) {
                break;
            }
            float h00 = corner(segment, surface - tiltX - tiltZ);
            float h10 = corner(segment, surface + tiltX - tiltZ);
            float h11 = corner(segment, surface + tiltX + tiltZ);
            float h01 = corner(segment, surface - tiltX + tiltZ);
            boolean last = i == segments.size() - 1;
            if (last || surface < segment.y1()) {
                renderTop(consumer, pose, segment, top, color, h00, h10, h11, h01, light, overlay);
            }
            renderBottom(consumer, pose, segment, top, color, light, overlay);
            renderSides(consumer, pose, segment, side, color, h00, h10, h11, h01, light, overlay);
        }
    }

    private static float surface(CocktailBlockEntity cocktail, Liquid liquid, float time) {
        float target = liquid.levels()[cocktail.getShownStage()];
        float progress = Mth.clamp((time - cocktail.getChangeTime()) / DRAIN_TICKS, 0.0F, 1.0F);
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress);
        return Mth.lerp(eased, cocktail.getFromLevel(), target);
    }

    private static float corner(Segment segment, float height) {
        return Mth.clamp(height, segment.y0(), segment.y1());
    }

    private static void renderTop(VertexConsumer consumer, PoseStack.Pose pose, Segment s, TextureAtlasSprite sprite, int color, float h00, float h10, float h11, float h01, int light, int overlay) {
        float[] uv = s.topUv();
        vertex(consumer, pose, s.x0(), h00, s.z0(), sprite, color, uv[0], uv[1], light, overlay, 0, 1, 0);
        vertex(consumer, pose, s.x0(), h01, s.z1(), sprite, color, uv[0], uv[3], light, overlay, 0, 1, 0);
        vertex(consumer, pose, s.x1(), h11, s.z1(), sprite, color, uv[2], uv[3], light, overlay, 0, 1, 0);
        vertex(consumer, pose, s.x1(), h10, s.z0(), sprite, color, uv[2], uv[1], light, overlay, 0, 1, 0);
    }

    private static void renderBottom(VertexConsumer consumer, PoseStack.Pose pose, Segment s, TextureAtlasSprite sprite, int color, int light, int overlay) {
        float[] uv = s.topUv();
        vertex(consumer, pose, s.x0(), s.y0(), s.z1(), sprite, color, uv[0], uv[3], light, overlay, 0, -1, 0);
        vertex(consumer, pose, s.x0(), s.y0(), s.z0(), sprite, color, uv[0], uv[1], light, overlay, 0, -1, 0);
        vertex(consumer, pose, s.x1(), s.y0(), s.z0(), sprite, color, uv[2], uv[1], light, overlay, 0, -1, 0);
        vertex(consumer, pose, s.x1(), s.y0(), s.z1(), sprite, color, uv[2], uv[3], light, overlay, 0, -1, 0);
    }

    private static void renderSides(VertexConsumer consumer, PoseStack.Pose pose, Segment s, TextureAtlasSprite sprite, int color, float h00, float h10, float h11, float h01, int light, int overlay) {
        float[] uv = s.sideUv();
        side(consumer, pose, s, sprite, color, uv, s.x1(), s.z0(), h10, s.x0(), s.z0(), h00, light, overlay, 0, -1);
        side(consumer, pose, s, sprite, color, uv, s.x0(), s.z1(), h01, s.x1(), s.z1(), h11, light, overlay, 0, 1);
        side(consumer, pose, s, sprite, color, uv, s.x0(), s.z0(), h00, s.x0(), s.z1(), h01, light, overlay, -1, 0);
        side(consumer, pose, s, sprite, color, uv, s.x1(), s.z1(), h11, s.x1(), s.z0(), h10, light, overlay, 1, 0);
    }

    private static void side(VertexConsumer consumer, PoseStack.Pose pose, Segment s, TextureAtlasSprite sprite, int color, float[] uv, float xa, float za, float ha, float xb, float zb, float hb, int light, int overlay, float nx, float nz) {
        vertex(consumer, pose, xa, ha, za, sprite, color, uv[0], uv[1], light, overlay, nx, 0, nz);
        vertex(consumer, pose, xa, s.y0(), za, sprite, color, uv[0], bottomV(s, uv, ha), light, overlay, nx, 0, nz);
        vertex(consumer, pose, xb, s.y0(), zb, sprite, color, uv[2], bottomV(s, uv, hb), light, overlay, nx, 0, nz);
        vertex(consumer, pose, xb, hb, zb, sprite, color, uv[2], uv[1], light, overlay, nx, 0, nz);
    }

    private static float bottomV(Segment s, float[] uv, float height) {
        return uv[1] + (uv[3] - uv[1]) * (height - s.y0()) / (s.y1() - s.y0());
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, TextureAtlasSprite sprite, int color, float u, float v, int light, int overlay, float nx, float ny, float nz) {
        consumer.addVertex(pose, x / 16.0F, y / 16.0F, z / 16.0F)
                .setColor(color)
                .setUv(sprite.getU(u / 16.0F), sprite.getV(v / 16.0F))
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }

    private record Liquid(ResourceLocation side, ResourceLocation top, float[] levels, List<Segment> segments) {
    }

    private record Segment(float x0, float z0, float x1, float z1, float y0, float y1, float[] sideUv, float[] topUv) {
    }
}
