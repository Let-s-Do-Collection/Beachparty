package net.satisfy.beachparty.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.beachparty.client.gui.handler.PalmBarGuiHandler;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class PalmBarGui extends AbstractRecipeBookGUIScreen<PalmBarGuiHandler> {
    public static final ResourceLocation BG = new BeachpartyIdentifier("textures/gui/palm_bar_gui.png");

    public static final int ARROW_Y = 45;
    public static final int ARROW_X = 94;

    public static final int SHAKE_Y = 42;
    public static final int SHAKE_X = 96;

    public PalmBarGui(PalmBarGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, BG);
    }

    @Override
    protected void init() {
        this.titleLabelX += 2;
        this.titleLabelY -= 3;
        super.init();
    }


    protected void renderProgressArrow(GuiGraphics guiGraphics) {
        final int progressX = this.menu.getShakeXProgress();
        guiGraphics.blit(BG, leftPos + ARROW_X, topPos + ARROW_Y, 177, 26, progressX, 10);

        final int progressY = menu.slots.get(0).hasItem() ? 20 : this.menu.getShakeYProgress();
        guiGraphics.blit(BG, leftPos + SHAKE_X, topPos + SHAKE_Y - progressY, 179, 22 - progressY, 15, progressY);
    }
}
