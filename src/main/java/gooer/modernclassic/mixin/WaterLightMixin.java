package gooer.modernclassic.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class WaterLightMixin
{


    @Shadow public abstract Block getBlock();

    @Inject(
            method = "getOpacity",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkForWaterBlock(BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {

        Block testedBlock = this.getBlock();

        if(testedBlock == Blocks.WATER ||
        testedBlock == Blocks.ICE ||
        testedBlock == Blocks.PACKED_ICE ||
        testedBlock == Blocks.BLUE_ICE ||
        testedBlock == Blocks.FROSTED_ICE ||
        testedBlock == Blocks.KELP ||
        testedBlock == Blocks.KELP_PLANT ||
        testedBlock == Blocks.SEAGRASS ||
        testedBlock == Blocks.TALL_SEAGRASS
        ){
            cir.setReturnValue(3);

        }

    }


}