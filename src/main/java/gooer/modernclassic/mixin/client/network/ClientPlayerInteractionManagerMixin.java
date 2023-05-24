package gooer.modernclassic.mixin.client.network;

import gooer.modernclassic.Modernclassic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {


    @Shadow private GameMode gameMode;

    @Shadow @Final private MinecraftClient client;

    /**
     * @author Max
     * @reason .
     */
    @Overwrite
    private ActionResult interactBlockInternal(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ActionResult actionResult;
        boolean bl2;
        BlockPos blockPos = hitResult.getBlockPos();
        ItemStack itemStack = player.getStackInHand(hand);
        if (this.gameMode == GameMode.SPECTATOR) {
            return ActionResult.SUCCESS;
        }
        boolean bl = !player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty();
        boolean bl3 = bl2 = player.shouldCancelInteraction() && bl && !Modernclassic.shiftUsedBlocks.contains(this.client.world.getBlockState(blockPos).getBlock());
        if (!bl2 && (actionResult = this.client.world.getBlockState(blockPos).onUse(this.client.world, player, hand, hitResult)).isAccepted()) {
            return actionResult;
        }
        if (itemStack.isEmpty() || player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
            return ActionResult.PASS;
        }
        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        if (this.gameMode.isCreative()) {
            int i = itemStack.getCount();
            actionResult = itemStack.useOnBlock(itemUsageContext);
            itemStack.setCount(i);
        } else {
            actionResult = itemStack.useOnBlock(itemUsageContext);
        }
        return actionResult;
    }
}