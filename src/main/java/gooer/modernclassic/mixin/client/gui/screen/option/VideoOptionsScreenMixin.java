package gooer.modernclassic.mixin.client.gui.screen.option;

import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {

    /**
     * @author Max
     * @reason Remove gamma slider
     */
    @Overwrite
    private static SimpleOption<?>[] getOptions(GameOptions gameOptions){
        return new SimpleOption[]{gameOptions.getGraphicsMode(), gameOptions.getViewDistance(),
                gameOptions.getChunkBuilderMode(), gameOptions.getSimulationDistance(),
                gameOptions.getAo(), gameOptions.getMaxFps(), gameOptions.getEnableVsync(),
                gameOptions.getBobView(), gameOptions.getGuiScale(), gameOptions.getAttackIndicator(),
                gameOptions.getCloudRenderMod(), gameOptions.getFullscreen(),
                gameOptions.getParticles(), gameOptions.getMipmapLevels(), gameOptions.getEntityShadows(),
                gameOptions.getDistortionEffectScale(), gameOptions.getEntityDistanceScaling(),
                gameOptions.getFovEffectScale(), gameOptions.getShowAutosaveIndicator()};
    }

}