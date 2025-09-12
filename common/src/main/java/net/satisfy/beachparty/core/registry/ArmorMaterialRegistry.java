package net.satisfy.beachparty.core.registry;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialRegistry {
    private static final int ENCHANTMENT_VALUE = 15;
    private static final Holder<SoundEvent> EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    private static final float TOUGHNESS = 0.0F;
    private static final float KNOCKBACK_RESISTANCE = 0.0F;
    public static final ArmorMaterial TRUNKS = createMaterial("trunks", Ingredient.of(Items.STRING), true);
    public static final ArmorMaterial BIKINI = createMaterial("bikini", Ingredient.of(Items.STRING), true);
    public static final ArmorMaterial RING = createMaterial("ring", Ingredient.of(Items.DRIED_KELP), false);
    public static final ArmorMaterial BEACH_HAT = createMaterial("beach_hat", Ingredient.of(Items.WHEAT), false);
    public static final ArmorMaterial SUNGLASSES = createMaterial("sunglasses", Ingredient.of(Blocks.BLACK_STAINED_GLASS_PANE.asItem()), false);
    public static final ArmorMaterial SWIM_WINGS = createMaterial("swim_wings", Ingredient.of(Items.DRIED_KELP), true);
    public static final ArmorMaterial CROCS = createMaterial("crocs", Ingredient.of(Items.DRIED_KELP), true);

    private static ArmorMaterial createMaterial(String name, Ingredient repairIngredient, boolean dyeable) {
        return register(name, Util.make(new EnumMap(ArmorItem.Type.class), (enumMap) -> {
            enumMap.put(ArmorItem.Type.BOOTS, 112);
            enumMap.put(ArmorItem.Type.LEGGINGS, 136);
            enumMap.put(ArmorItem.Type.CHESTPLATE, 144);
            enumMap.put(ArmorItem.Type.HELMET, 128);
            enumMap.put(ArmorItem.Type.BODY, 3);
        }), ENCHANTMENT_VALUE, EQUIP_SOUND, TOUGHNESS, KNOCKBACK_RESISTANCE, () -> {
            return repairIngredient;
        }, List.of(new ArmorMaterial.Layer(BeachpartyIdentifier.identifier(name), "", true), new ArmorMaterial.Layer(BeachpartyIdentifier.identifier(name), "_overlay", dyeable)));
    }

    private static ArmorMaterial register(String string, EnumMap<ArmorItem.Type, Integer> enumMap, int i, Holder<SoundEvent> arg, float f, float g, Supplier<Ingredient> supplier, List<ArmorMaterial.Layer> list) {
        EnumMap<ArmorItem.Type, Integer> enumMap2 = new EnumMap<>(ArmorItem.Type.class);
        ArmorItem.Type[] var9 = ArmorItem.Type.values();

        for (ArmorItem.Type type : var9) {
            enumMap2.put(type, enumMap.get(type));
        }

        return new ArmorMaterial(enumMap2, i, arg, supplier, list, f, g);
    }
}
