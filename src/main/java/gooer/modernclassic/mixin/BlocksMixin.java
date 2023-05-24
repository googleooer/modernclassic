package gooer.modernclassic.mixin;

import net.minecraft.block.*;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.ToIntFunction;

@Mixin(Blocks.class)
public class BlocksMixin {


    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torch")),
            at = @At(value = "INVOKE", target = "net/minecraft/block/TorchBlock.<init> (Lnet/minecraft/block/AbstractBlock$Settings;Lnet/minecraft/particle/ParticleEffect;)V", ordinal = 0))
    private static AbstractBlock.Settings TorchRegistry(AbstractBlock.Settings settings){ return AbstractBlock.Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 13).sounds(BlockSoundGroup.WOOD); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=wall_torch")),
            at = @At(value = "INVOKE", target = "net/minecraft/block/WallTorchBlock.<init> (Lnet/minecraft/block/AbstractBlock$Settings;Lnet/minecraft/particle/ParticleEffect;)V", ordinal = 0))
    private static AbstractBlock.Settings WallTorchRegistry(AbstractBlock.Settings settings){ return AbstractBlock.Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 13).sounds(BlockSoundGroup.WOOD).dropsLike(Blocks.TORCH); }





}