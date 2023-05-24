package gooer.modernclassic.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityStuffMixin
extends AbstractClientPlayerEntity {

    public ClientPlayerEntityStuffMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    /**
     * @author Max
     * @reason Removes weird neon blue fog when the player is underwater
     */
    @Overwrite
    public float getUnderwaterVisibility(){
        return 0.0f;
    }
}