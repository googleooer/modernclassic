package gooer.modernclassic.mixin.client.option;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    //@Shadow @Final private final SimpleOption<Double> gamma;


    /*
    public GameOptionsMixin(SimpleOption<Double> gamma) {
        this.gamma = gamma;
    }

     */


    //@ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=options.gamma")),
    //at = @At(value = "INVOKE", target = ""))

    //@Inject(method = "<clinit>", at = @At(value = "RETURN", target = "method_42492(Lnet/minecraft/text/Text;Ljava/lang/Double;)Lnet/minecraft/text/Text"))
    //private static void redirectGammaOption(CallbackInfo ci){ }

    @Inject(method = "method_42492", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    private static void overrideGamma(Text optionText, Double value, CallbackInfoReturnable<Text> cir){


        cir.setReturnValue(GameOptions.getGenericValueText(optionText, (int)(value * 100.0f - 50.0f)));
    }

    //@Inject(method="<clinit>", at = @At(value = "FIELD", target = ""))

    /**
     * @author Max
     * @reason Overwrite gamma
     */
    @Overwrite
    public SimpleOption<Double> getGamma() {

        return new SimpleOption<Double>("options.gamma", SimpleOption.emptyTooltip(), (optionText, value) -> {
            value = 0.0;
            int i = (int)(value * 100.0);

            if (i == 0) {
                return GameOptions.getGenericValueText(optionText, Text.translatable("options.gamma.min"));
            }
            if (i == 50) {
                return GameOptions.getGenericValueText(optionText, Text.translatable("options.gamma.default"));
            }
            if (i == 100) {
                return GameOptions.getGenericValueText(optionText, Text.translatable("options.gamma.max"));
            }
            return GameOptions.getGenericValueText(optionText, i);
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, value -> {});
    }



    //@Redirect(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;gamma:Lnet/minecraft/client/option/SimpleOption", opcode = Opcodes.PUTFIELD))
    //private static final void removeGamma(CallbackInfo ci){

    //}

}