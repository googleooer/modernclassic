package gooer.modernclassic.mixin.screen;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

import static net.minecraft.screen.AnvilScreenHandler.getNextCost;
import static net.minecraft.util.math.MathHelper.floor;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin
extends ForgingScreenHandler {
    //TODO: Remove too expensive for repairing, raise too expensive limit


    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Shadow private String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }



    /**
     * @author Max
     * @reason
     */
    @Overwrite
    public void updateResult(){
        ItemStack itemStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemStack.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
            return;
        }
        ItemStack itemStack2 = itemStack.copy();
        ItemStack itemStack3 = this.input.getStack(1);
        Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack2);
        /*if(itemStack3.getItem().canRepair(itemStack,itemStack3) && !itemStack3.hasEnchantments()) {
            final float difficulty_modifier =
                    switch (player.world.getDifficulty()) {
                        case PEACEFUL -> 0.25f;
                        case EASY -> 0.75f;
                        case NORMAL -> 1.0f;
                        case HARD -> 1.5f;
                    };
            j = floor(5 * difficulty_modifier);
        } else {*/
            j += itemStack.getRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getRepairCost());
        //}
        this.repairItemUsage = 0;
        if (!itemStack3.isEmpty()) {
            boolean bl;
            boolean bl2 = bl = itemStack3.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(itemStack3).isEmpty();
            if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                int m;
                int l = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                if (l <= 0) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                for (m = 0; l > 0 && m < itemStack3.getCount(); ++m) {
                    int n = itemStack2.getDamage() - l;
                    itemStack2.setDamage(n);
                    ++i;
                    l = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                }
                this.repairItemUsage = 0;
            } else {
                if (!(bl || itemStack2.isOf(itemStack3.getItem()) && itemStack2.isDamageable())) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                if (itemStack2.isDamageable() && !bl) {
                    int l = itemStack.getMaxDamage() - itemStack.getDamage();
                    int m = itemStack3.getMaxDamage() - itemStack3.getDamage();
                    int n = m + itemStack2.getMaxDamage() * 12 / 100;
                    int o = l + n;
                    int p = itemStack2.getMaxDamage() - o;
                    if (p < 0) {
                        p = 0;
                    }
                    if (p < itemStack2.getDamage()) {
                        itemStack2.setDamage(p);
                        i += 2;
                    }
                }
                Map<Enchantment, Integer> map2 = EnchantmentHelper.get(itemStack3);
                boolean bl22 = false;
                boolean bl3 = false;
                for (Enchantment enchantment : map2.keySet()) {
                    int r;
                    if (enchantment == null) continue;
                    int q = map.getOrDefault(enchantment, 0);
                    r = q == (r = map2.get(enchantment).intValue()) ? r + 1 : Math.max(r, q);
                    boolean bl4 = enchantment.isAcceptableItem(itemStack);
                    if (this.player.getAbilities().creativeMode || itemStack.isOf(Items.ENCHANTED_BOOK)) {
                        bl4 = true;
                    }
                    for (Enchantment enchantment2 : map.keySet()) {
                        if (enchantment2 == enchantment || enchantment.canCombine(enchantment2)) continue;
                        bl4 = false;
                        ++i;
                    }
                    if (!bl4) {
                        bl3 = true;
                        continue;
                    }
                    bl22 = true;
                    if (r > enchantment.getMaxLevel()) {
                        r = enchantment.getMaxLevel();
                    }
                    map.put(enchantment, r);
                    int s = 0;
                    switch (enchantment.getRarity()) {
                        case COMMON: {
                            s = 1;
                            break;
                        }
                        case UNCOMMON: {
                            s = 2;
                            break;
                        }
                        case RARE: {
                            s = 4;
                            break;
                        }
                        case VERY_RARE: {
                            s = 8;
                        }
                    }
                    if (bl) {
                        s = Math.max(1, s / 2);
                    }
                    i += s * r;
                    if (itemStack.getCount() <= 1) continue;
                    i = 40;
                }
                if (bl3 && !bl22) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
            }
        }
        if (StringUtils.isBlank(this.newItemName)) {
            if (itemStack.hasCustomName()) {
                k = 1;
                i += k;
                itemStack2.removeCustomName();
            }
        } else if (!this.newItemName.equals(itemStack.getName().getString())) {
            k = 1;
            i += k;
            itemStack2.setCustomName(Text.literal(this.newItemName));
        }
        this.levelCost.set(j + i);
        if (i <= 0) {
            itemStack2 = ItemStack.EMPTY;
        }
        if (k == i && k > 0 && this.levelCost.get() >= 40) {
            this.levelCost.set(39);
        }
        if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
            itemStack2 = ItemStack.EMPTY;
        }
        if (!itemStack2.isEmpty()) {
            //boolean isRepairItem = itemStack3.getItem().canRepair(itemStack,itemStack3) && !itemStack3.hasEnchantments();
            int t = itemStack2.getRepairCost();
            if (!itemStack3.isEmpty() && t < itemStack3.getRepairCost()) {
                t = itemStack3.getRepairCost();
            }
            if (k != i || k == 0) {
                t = getNextCost(t);
            }
            itemStack2.setRepairCost(t);


            EnchantmentHelper.set(map, itemStack2);
        }
        this.output.setStack(0, itemStack2);
        this.sendContentUpdates();
    }

    /**
     * @author Max
     * @reason Make it so repairing doesn't increase cost.
     */
    @Overwrite
    public static int getNextCost(int cost){
        return cost + 4;
    }

}