package net.satisfy.beachparty.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.item.IBeachpartyArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    protected HandledScreenMixin() {
        super(null);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 1) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
            for (Slot slot : screen.getMenu().slots) {
                if (beachparty$isMouseOverSlot(slot, mouseX, mouseY)) {
                    ItemStack itemStack = slot.getItem();
                    if (itemStack.getItem() instanceof IBeachpartyArmorItem beachpartyArmor) {
                        beachpartyArmor.toggleVisibility(itemStack);
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
        }
    }


    @Unique
    private boolean beachparty$isMouseOverSlot(Slot slot, double mouseX, double mouseY) {
        int slotX = this.leftPos + slot.x;
        int slotY = this.topPos + slot.y;
        return mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 16;
    }
}
