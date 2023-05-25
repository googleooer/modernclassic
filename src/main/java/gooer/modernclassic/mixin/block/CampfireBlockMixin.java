package gooer.modernclassic.mixin.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
        extends BlockWithEntity
implements Waterloggable {

    @Shadow @Final protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    @Shadow @Final public static final BooleanProperty LIT = Properties.LIT;
    @Shadow @Final public static final BooleanProperty SIGNAL_FIRE = Properties.SIGNAL_FIRE;
    @Shadow @Final public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    @Shadow @Final public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    /**
     * The shape used to test whether a given block is considered 'smokey'.
     */
    @Shadow @Final private static final VoxelShape SMOKEY_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    @Shadow @Final private static final int field_31049 = 5;

    protected CampfireBlockMixin(Settings settings) {
        super(settings);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CampfireBlockEntity(pos, state);
    }

    /**
     * @author Max
     * @reason Removes cooking interaction
     */
    @Overwrite
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    return ActionResult.PASS;
    }


    /**
     * @author Max
     * @reason Removes cooking interaction
     */
    @Overwrite
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }


    /**
     * @author Max
     * @reason .
     */
    @Overwrite
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (state.get(LIT).booleanValue()) {
                return CampfireBlock.checkType(type, BlockEntityType.CAMPFIRE, CampfireBlockEntity::litServerTick);
            }

        return null;
    }



}