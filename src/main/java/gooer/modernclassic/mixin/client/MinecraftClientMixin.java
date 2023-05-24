package gooer.modernclassic.mixin.client;

import gooer.modernclassic.Modernclassic;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    /**
     * @author Max
     * @reason Replace title
     */
    @Overwrite
    private String getWindowTitle(){
        StringBuilder stringBuilder = new StringBuilder("Beta Rewind");
        stringBuilder.append(" ");
        stringBuilder.append(Modernclassic.CURR_VERSION);
        stringBuilder.append(" (Minecraft ");
        stringBuilder.append(SharedConstants.getGameVersion().getName());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}