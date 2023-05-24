package gooer.modernclassic.block;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.block.entity.FatigueBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FatigueBlock
extends BlockWithEntity {
    public FatigueBlock(Settings settings) {super(settings);}

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {return new FatigueBlockEntity(pos,state);}

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {return FatigueBlock.checkType(type, Modernclassic.FATIGUE_BLOCK_ENTITY, FatigueBlockEntity::tick);}
}