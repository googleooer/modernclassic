package gooer.modernclassic.block.entity;

import gooer.modernclassic.Modernclassic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;



/*
public class ReworkedCampfireBlockEntity
extends BlockEntity {

    public ReworkedCampfireBlockEntity(BlockPos pos, BlockState state) { super(Modernclassic.REWORKED_CAMPFIRE_BLOCK_ENTITY, pos, state); }

    public static void tick(World world, BlockPos pos, BlockState state, ReworkedCampfireBlockEntity blockEntity){
        if(world.getTime() % 80L == 0L){
        ReworkedCampfireBlockEntity.updatePlayerEffects(world, pos);

        }

    }

    public static void updatePlayerEffects(World world, BlockPos pos){
        if(world.isClient){return;}

        Box box = new Box(pos).expand(16.0D, 32.0D, 16.0D);

        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
        for(PlayerEntity playerEntity : list){
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 15,1));
        }

    }

}

*/