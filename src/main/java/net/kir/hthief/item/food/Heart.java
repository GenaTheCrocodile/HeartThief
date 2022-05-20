package net.kir.hthief.item.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Heart extends Item {
    public Heart(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient()) {
            EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            assert maxHealth != null;
            if (maxHealth.getBaseValue() < 40)
                maxHealth.setBaseValue(maxHealth.getBaseValue() + 2);
        }
        return super.finishUsing(stack, world, player);
    }
}
