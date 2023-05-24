package gooer.modernclassic.block.entity;

import gooer.modernclassic.Modernclassic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class FatigueBlockEntity
extends BlockEntity {
    public FatigueBlockEntity(BlockPos pos, BlockState state) {super(Modernclassic.FATIGUE_BLOCK_ENTITY, pos, state);}

    public static void tick(World world, BlockPos pos, BlockState state, FatigueBlockEntity blockEntity){
        if(world.getTime() % 80L == 0L) updatePlayerEffects(world,pos);
    }

    private static void updatePlayerEffects(World world, BlockPos pos){
        if(world.isClient) return;
        Box box = new Box(pos).expand(16.0D, 32.0D, 16.0D);

        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
        for(PlayerEntity playerEntity : list){
            if(!playerEntity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {

                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                Modernclassic.LOGGER.info("Tried to play fatigue sound.");
            } else {
                Modernclassic.LOGGER.info("Did not play fatigue sound.");
            }

            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 300,2, true, false));
        }

    }

}