package net.satisfy.beachparty.client.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IngredientColors {
    private static final int FALLBACK = 0xFFE8E8E8;
    private static final Map<Item, Integer> CACHE = new HashMap<>();

    private IngredientColors() {
    }

    public static int get(ItemStack stack) {
        return CACHE.computeIfAbsent(stack.getItem(), item -> sample(stack));
    }

    public static int mix(List<ItemStack> stacks) {
        if (stacks.isEmpty()) {
            return FALLBACK;
        }
        int r = 0;
        int g = 0;
        int b = 0;
        for (ItemStack stack : stacks) {
            int color = get(stack);
            r += FastColor.ARGB32.red(color);
            g += FastColor.ARGB32.green(color);
            b += FastColor.ARGB32.blue(color);
        }
        int n = stacks.size();
        return FastColor.ARGB32.color(255, r / n, g / n, b / n);
    }

    private static int sample(ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        ResourceLocation sprite = minecraft.getItemRenderer().getItemModelShaper().getItemModel(stack).getParticleIcon().contents().name();
        ResourceLocation file = sprite.withPath(path -> "textures/" + path + ".png");
        try (InputStream in = minecraft.getResourceManager().open(file); NativeImage image = NativeImage.read(in)) {
            int size = Math.min(image.getWidth(), image.getHeight());
            long r = 0;
            long g = 0;
            long b = 0;
            int count = 0;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    int abgr = image.getPixelRGBA(x, y);
                    if ((abgr >>> 24) < 128) {
                        continue;
                    }
                    r += abgr & 0xFF;
                    g += (abgr >> 8) & 0xFF;
                    b += (abgr >> 16) & 0xFF;
                    count++;
                }
            }
            if (count == 0) {
                return FALLBACK;
            }
            return FastColor.ARGB32.color(255, (int) (r / count), (int) (g / count), (int) (b / count));
        } catch (Exception e) {
            return FALLBACK;
        }
    }
}
