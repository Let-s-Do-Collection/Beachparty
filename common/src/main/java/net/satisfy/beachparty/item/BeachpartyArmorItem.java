package net.satisfy.beachparty.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.NotNull;

public class BeachpartyArmorItem extends ArmorItem {
    private final ResourceLocation getTexture;

    public BeachpartyArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties, ResourceLocation getTexture) {
        super(armorMaterial, type, properties);
        this.getTexture = getTexture;
    }

    public ResourceLocation getTexture() {
        return getTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }
}
