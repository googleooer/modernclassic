package gooer.modernclassic.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

import static net.minecraft.server.command.EnchantCommand.*;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.translatable("commands.enchant.failed.entity", entityName));
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.translatable("commands.enchant.failed.itemless", entityName));
    private static final DynamicCommandExceptionType FAILED_INCOMPATIBLE_EXCEPTION = new DynamicCommandExceptionType(itemName -> Text.translatable("commands.enchant.failed.incompatible", itemName));
    private static final Dynamic2CommandExceptionType FAILED_LEVEL_EXCEPTION = new Dynamic2CommandExceptionType((level, maxLevel) -> Text.translatable("commands.enchant.failed.level", level, maxLevel));
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.enchant.failed"));


    /**
     * @author Max
     * @reason Remove enchanting limits
     */
    @Overwrite
    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets, Enchantment enchantment, int level) throws CommandSyntaxException {
        //if (level > enchantment.getMaxLevel()) {
            //throw FAILED_LEVEL_EXCEPTION.create(level, enchantment.getMaxLevel());
        //}
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                ItemStack itemStack = livingEntity.getMainHandStack();
                if (!itemStack.isEmpty()) {
                    //if (enchantment.isAcceptableItem(itemStack) && EnchantmentHelper.isCompatible(EnchantmentHelper.get(itemStack).keySet(), enchantment)) {
                        itemStack.addEnchantment(enchantment, level);
                        ++i;
                        //continue;
                    //}
                    if (targets.size() != 1) continue;
                    //throw FAILED_INCOMPATIBLE_EXCEPTION.create(itemStack.getItem().getName(itemStack).getString());
                }
                if (targets.size() != 1) continue;
                throw FAILED_ITEMLESS_EXCEPTION.create(livingEntity.getName().getString());
            }
            if (targets.size() != 1) continue;
            throw FAILED_ENTITY_EXCEPTION.create(entity.getName().getString());
        }
        if (i == 0) {
            throw FAILED_EXCEPTION.create();
        }
        if (targets.size() == 1) {
            source.sendFeedback(Text.translatable("commands.enchant.success.single", enchantment.getName(level), targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(Text.translatable("commands.enchant.success.multiple", enchantment.getName(level), targets.size()), true);
        }
        return i;
    }


}