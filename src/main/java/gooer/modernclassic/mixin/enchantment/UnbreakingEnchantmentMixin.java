package gooer.modernclassic.mixin.enchantment;

import net.minecraft.enchantment.*;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UnbreakingEnchantment.class)
public class UnbreakingEnchantmentMixin
extends Enchantment {


    protected UnbreakingEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public  boolean canAccept(Enchantment other){ return super.canAccept(other) && other != Enchantments.MENDING; }
}