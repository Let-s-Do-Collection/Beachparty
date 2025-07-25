package net.satisfy.beachparty.core.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class ArmorMaterialRegistry {
    private static final int ENCHANTMENT_VALUE = 15;
    private static final SoundEvent EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    private static final float TOUGHNESS = 0.0F;
    private static final float KNOCKBACK_RESISTANCE = 0.0F;
    public static final ArmorMaterial TRUNKS = createMaterial("trunks", Ingredient.of(Items.STRING));
    public static final ArmorMaterial BIKINI = createMaterial("bikini", Ingredient.of(Items.STRING));
    public static final ArmorMaterial RING = createMaterial("ring", Ingredient.of(Items.DRIED_KELP));
    public static final ArmorMaterial BEACH_HAT = createMaterial("beach_hat", Ingredient.of(Items.WHEAT));
    public static final ArmorMaterial SUNGLASSES = createMaterial("sunglasses", Ingredient.of(Blocks.BLACK_STAINED_GLASS_PANE.asItem()));
    public static final ArmorMaterial SWIM_WINGS = createMaterial("swim_wings", Ingredient.of(Items.DRIED_KELP));
    public static final ArmorMaterial CROCS = createMaterial("crocs", Ingredient.of(Items.DRIED_KELP));

    private static ArmorMaterial createMaterial(String name, Ingredient repairIngredient) {

        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 128;
                    case CHESTPLATE -> 144;
                    case LEGGINGS -> 136;
                    case BOOTS -> 112;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET, LEGGINGS, BOOTS, CHESTPLATE -> 1;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return ENCHANTMENT_VALUE;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return EQUIP_SOUND;
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return repairIngredient;
            }

            @Override
            public @NotNull String getName() {
                return name;
            }

            @Override
            public float getToughness() {
                return TOUGHNESS;
            }

            @Override
            public float getKnockbackResistance() {
                return KNOCKBACK_RESISTANCE;
            }
        };
    }
}
