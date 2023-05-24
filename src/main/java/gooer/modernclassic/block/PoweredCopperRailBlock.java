package gooer.modernclassic.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;

public class PoweredCopperRailBlock
extends PoweredRailBlock {

    public PoweredCopperRailBlock(AbstractBlock.Settings settings){
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SHAPE, RailShape.NORTH_SOUTH)).with(POWERED, false)).with(WATERLOGGED, false));
    }



}