package gooer.modernclassic.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {

    @Shadow @Final static IntProperty NOTE;

    @Shadow protected abstract void playNote(@Nullable Entity entity, World world, BlockPos pos);

    /**
     * @author Max
     * @reason .
     */
    @Overwrite
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }


        state = Screen.hasShiftDown() && player.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()? (BlockState)state.with(NOTE, state.get(NOTE) == 0? 24 : state.get(NOTE)-1) :(BlockState)state.cycle(NOTE);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        this.playNote(player, world, pos);
        player.incrementStat(Stats.TUNE_NOTEBLOCK);
        return ActionResult.CONSUME;
    }



}