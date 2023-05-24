package gooer.modernclassic.mixin.client.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static gooer.modernclassic.Modernclassic.ALLOW_SPRINTING;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin
extends Entity {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public int ticksSinceSprintingChanged;

    public ClientPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author Max
     * @reason Make sprinting gamerule-based
     */
    @Overwrite
    public void setSprinting(boolean sprinting) {
        if(this.world.getGameRules().getBoolean(ALLOW_SPRINTING)){
            super.setSprinting(sprinting);
            this.ticksSinceSprintingChanged = 0;
        }
        //No :cornsyrup:
    }

}