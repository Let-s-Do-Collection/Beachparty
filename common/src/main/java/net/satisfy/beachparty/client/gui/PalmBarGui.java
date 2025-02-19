package net.satisfy.beachparty.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.beachparty.client.gui.handler.PalmBarGuiHandler;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class PalmBarGui extends AbstractContainerScreen<PalmBarGuiHandler> {
    public static final ResourceLocation BG = new BeachpartyIdentifier("textures/gui/palm_bar_gui.png");
    public static final int ARROW_Y = 45;
    public static final int ARROW_X = 94;
    public static final int SHAKE_Y = 42;
    public static final int SHAKE_X = 96;

    public PalmBarGui(PalmBarGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX += 2;
        titleLabelY -= 3;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BG);
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(guiGraphics);
    }

    protected void renderProgressArrow(GuiGraphics guiGraphics) {
        int progressX = menu.getShakeXProgress();
        guiGraphics.blit(BG, leftPos + ARROW_X, topPos + ARROW_Y, 177, 26, progressX, 10);
        int progressY = menu.isOutputSlotFilled() ? 20 : menu.getShakeYProgress();
        guiGraphics.blit(BG, leftPos + SHAKE_X, topPos + SHAKE_Y - progressY, 179, 22 - progressY, 15, progressY);
    }
}
