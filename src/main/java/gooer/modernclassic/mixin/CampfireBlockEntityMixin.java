package gooer.modernclassic.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin
        extends BlockEntity
        implements Clearable {




    public CampfireBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void clear() {

    }

    /**
     * @author Max
     * @reason Remove cooking
     */
    @Overwrite
    public static void litServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire) {
        if(world.getTime() % 80L == 0L){
            updatePlayerEffects(world, pos);
        }
    }



    private static void updatePlayerEffects(World world, BlockPos pos){
        if(world.isClient){return;}

        Box box = new Box(pos).expand(16.0D, 32.0D, 16.0D);

        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
        for(PlayerEntity playerEntity : list){
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300,0, true, true));
        }

    }
}