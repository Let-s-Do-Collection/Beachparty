package net.satisfy.beachparty.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ArmorMaterialRegistry {
    private static final int ENCHANTMENT_VALUE = 15;
    private static final Holder<SoundEvent> EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    private static final float TOUGHNESS = 0.0F;
    private static final float KNOCKBACK_RESISTANCE = 0.0F;
    private static final Map<String, ArmorMaterial> CACHE = new ConcurrentHashMap<>();

    public static ArmorMaterial material(String baseName, Supplier<Ingredient> repair, boolean dyeable) {
        ArmorMaterial cached = CACHE.get(baseName);
        if (cached != null) return cached;
        ResourceLocation base = BeachpartyIdentifier.identifier(baseName);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(base, "", dyeable));
        ArmorMaterial created = register(slots(1, 1, 1, 1, 1), ENCHANTMENT_VALUE, EQUIP_SOUND, TOUGHNESS, KNOCKBACK_RESISTANCE, repair, layers);
        CACHE.put(baseName, created);
        return created;
    }

    public static ArmorMaterial materialWithOverlay(String baseName, Supplier<Ingredient> repair, boolean dyeable) {
        ResourceLocation base = BeachpartyIdentifier.identifier(baseName);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(base, "", dyeable), new ArmorMaterial.Layer(base, "_overlay", false));
        return register(slots(1, 1, 1, 1, 1), ENCHANTMENT_VALUE, EQUIP_SOUND, TOUGHNESS, KNOCKBACK_RESISTANCE, repair, layers);
    }

    private static EnumMap<ArmorItem.Type, Integer> slots(int boots, int leggings, int chestplate, int helmet, int body) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.BODY, body);
        return map;
    }

    private static ArmorMaterial register(EnumMap<ArmorItem.Type, Integer> health, int enchantValue, Holder<SoundEvent> equipSound, float toughness, float knockback, Supplier<Ingredient> repair, List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> copy = new EnumMap<>(ArmorItem.Type.class);
        copy.putAll(health);
        return new ArmorMaterial(copy, enchantValue, equipSound, repair, layers, toughness, knockback);
    }
}