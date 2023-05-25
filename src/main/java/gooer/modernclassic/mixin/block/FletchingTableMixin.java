package gooer.modernclassic.mixin.block;


import gooer.modernclassic.screen.FletchingTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FletchingTableBlock.class)
public class FletchingTableMixin extends CraftingTableBlock {

    private static final Text SCREEN_TITLE = Text.translatable("container.fletching");

    public FletchingTableMixin(Settings settings) {
        super(settings);
    }


    //@Overwrite
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new FletchingTableScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), SCREEN_TITLE);
    }

    /*@Inject(
            method = "onUse",
            at = @At("RETURN"), cancellable = true)
    private void fletchingInjection(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        if(world.isClient){
            cir.setReturnValue(ActionResult.SUCCESS);
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        //TODO: create stat for how many times the we've interacted with fletching tables
        //player.incrementStat(Stats.INTERACT_WITH_FLETCHING_TABLE);
        cir.setReturnValue(ActionResult.CONSUME);
    }*/

    /**
     * @author Max
     * @reason Adds right click functionality to the fletching table.
     */
    @Overwrite
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world,pos));
        //TODO: create stat for how many times the we've interacted with fletching tables
        //player.incrementStat(Stats.INTERACT_WITH_FLETCHING_TABLE);
        return ActionResult.CONSUME;

    }


}