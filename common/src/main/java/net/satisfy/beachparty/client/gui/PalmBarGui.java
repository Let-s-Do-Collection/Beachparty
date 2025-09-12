package net.satisfy.beachparty.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.beachparty.client.gui.handler.PalmBarGuiHandler;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class PalmBarGui extends AbstractContainerScreen<PalmBarGuiHandler> {
    public static final ResourceLocation BG = BeachpartyIdentifier.identifier("textures/gui/palm_bar_gui.png");
    public static final int ARROW_Y = 35;
    public static final int ARROW_X = 79;

    public PalmBarGui(PalmBarGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics g, float f, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        g.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int w = menu.getShakeXProgress();
        g.blit(BG, leftPos + ARROW_X, topPos + ARROW_Y, 177, 14, w, 14);
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        renderBackground(g, mx, my, dt);
        super.render(g, mx, my, dt);
        renderTooltip(g, mx, my);
    }
}
