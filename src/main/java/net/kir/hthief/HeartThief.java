package net.kir.hthief;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.kir.hthief.item.food.Heart;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HeartThief implements ModInitializer {
	public static final String MOD_ID = "hthief";

	public static final Item HEART = new Heart(new FabricItemSettings().group(ItemGroup.FOOD)
			.food(new FoodComponent.Builder()
					.alwaysEdible()
					.statusEffect(
							new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 10), 1)
					.statusEffect(
							new StatusEffectInstance(StatusEffects.POISON, 20 * 5), 1)
					.build()));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "heart"), HEART);

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			EntityAttributeInstance maxHealth = newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			assert maxHealth != null;
			float oldMaxHealth = oldPlayer.getMaxHealth();
			if (oldMaxHealth <= 2) {
				maxHealth.setBaseValue(2);
			} else {
				maxHealth.setBaseValue(oldPlayer.getMaxHealth() - 2);
			}
		});

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof PlayerEntity player) {
				if (killedEntity instanceof PlayerEntity) {
					EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
					assert maxHealth != null;
					maxHealth.setBaseValue(player.getMaxHealth() + 2);
				} else if (killedEntity instanceof VillagerEntity villagerEntity) {
					villagerEntity.dropItem(HEART);
				}
			}
		});
	}
}
