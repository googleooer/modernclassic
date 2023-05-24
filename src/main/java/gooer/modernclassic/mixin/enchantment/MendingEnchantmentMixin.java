package gooer.modernclassic.mixin.enchantment;

import net.minecraft.enchantment.*;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MendingEnchantment.class)
public class MendingEnchantmentMixin
extends Enchantment {

    protected MendingEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public  boolean canAccept(Enchantment other){ return super.canAccept(other) && other != Enchantments.UNBREAKING; }

}