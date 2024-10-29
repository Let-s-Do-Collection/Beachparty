package net.satisfy.beachparty.fabric.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.fabric.compat.BeachpartyTrinket;
import net.satisfy.beachparty.registry.ObjectRegistry;

import java.util.UUID;

public class BeachpartyEvents {
    private static final UUID SWIM_SPEED_MODIFIER_UUID = UUID.fromString("e8b3c6f3-1d2e-4f8b-9c3a-123456789abc");
    private static final UUID RUBBER_RING_SWIM_SPEED_MODIFIER_UUID = UUID.fromString("f4c3b6d7-8a9b-4c3e-8d2a-abcdef123456");
    private static final UUID RUBBER_RING_DIVE_ABILITY_MODIFIER_UUID = UUID.fromString("12345678-90ab-cdef-1234-567890abcdef");

    private static final double SWIM_SPEED_BOOST = 0.10;
    private static final double RUBBER_RING_SWIM_SPEED_BOOST = 0.25;
    private static final double DIVE_SPEED_BOOST = -0.05;

    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (Player player : server.getPlayerList().getPlayers()) {
                handlePlayerMovement(player);
                handleSwimSpeed(player);
                handleDiveAbility(player);
            }
        });
    }

    private static void handlePlayerMovement(Player player) {

        if (player.isSprinting() && BeachpartyTrinket.isTrinketEquipped(player, ObjectRegistry.CROCS.get())) {
            Level level = player.level();
            Vec3 pos = player.position();


            if (level.getFluidState(player.blockPosition().below()).is(FluidTags.WATER)) {

                player.setPos(pos.x, pos.y + 0.05, pos.z);


                Vec3 currentVelocity = player.getDeltaMovement();
                player.setDeltaMovement(new Vec3(currentVelocity.x, 0, currentVelocity.z));
            }
        }
    }

    private static void handleSwimSpeed(Player player) {
        boolean isSwimming = player.isSwimming();
        boolean hasSwimSuit = BeachpartyTrinket.isTrinketEquipped(player,
                ObjectRegistry.TRUNKS.get(),
                ObjectRegistry.BIKINI.get()
        );
        boolean hasRubberRing = BeachpartyTrinket.isTrinketEquipped(player,
                ObjectRegistry.RUBBER_RING_AXOLOTL.get(),
                ObjectRegistry.RUBBER_RING_BLUE.get(),
                ObjectRegistry.RUBBER_RING_PELICAN.get(),
                ObjectRegistry.RUBBER_RING_PINK.get(),
                ObjectRegistry.RUBBER_RING_STRIPPED.get()
        );

        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) return;

        if (isSwimming && hasSwimSuit) {
            if (movementSpeed.getModifier(SWIM_SPEED_MODIFIER_UUID) == null) {
                movementSpeed.addTransientModifier(new AttributeModifier(
                        SWIM_SPEED_MODIFIER_UUID,
                        "Swim Suit Speed Boost",
                        SWIM_SPEED_BOOST,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
            }
        } else {
            movementSpeed.removeModifier(SWIM_SPEED_MODIFIER_UUID);
        }


        if (isSwimming && hasRubberRing) {
            if (movementSpeed.getModifier(RUBBER_RING_SWIM_SPEED_MODIFIER_UUID) == null) {
                movementSpeed.addTransientModifier(new AttributeModifier(
                        RUBBER_RING_SWIM_SPEED_MODIFIER_UUID,
                        "Rubber Ring Speed Boost",
                        RUBBER_RING_SWIM_SPEED_BOOST,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
            }
        } else {
            movementSpeed.removeModifier(RUBBER_RING_SWIM_SPEED_MODIFIER_UUID);
        }
    }

    private static void handleDiveAbility(Player player) {
        boolean isSwimming = player.isSwimming();
        boolean hasRubberRing = BeachpartyTrinket.isTrinketEquipped(player,
                ObjectRegistry.RUBBER_RING_AXOLOTL.get(),
                ObjectRegistry.RUBBER_RING_BLUE.get(),
                ObjectRegistry.RUBBER_RING_PELICAN.get(),
                ObjectRegistry.RUBBER_RING_PINK.get(),
                ObjectRegistry.RUBBER_RING_STRIPPED.get()
        );

        AttributeInstance diveAbility = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (diveAbility == null) return;

        if (isSwimming && hasRubberRing) {
            if (diveAbility.getModifier(RUBBER_RING_DIVE_ABILITY_MODIFIER_UUID) == null) {
                diveAbility.addTransientModifier(new AttributeModifier(
                        RUBBER_RING_DIVE_ABILITY_MODIFIER_UUID,
                        "Rubber Ring Dive Ability",
                        DIVE_SPEED_BOOST,
                        AttributeModifier.Operation.ADDITION
                ));
            }
        } else {
            diveAbility.removeModifier(RUBBER_RING_DIVE_ABILITY_MODIFIER_UUID);
        }
    }
}
