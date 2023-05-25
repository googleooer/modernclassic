package gooer.modernclassic.mixin.server.integrated;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.client.gui.screen.IntegratedServerLoaderWarningScreen;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.BackupPromptScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.DatapackFailureScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.SaveLoading;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(IntegratedServerLoader.class)
public abstract class IntegratedServerLoaderMixin {



    @Inject(method = "start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V", at = @At("HEAD"))
    private void modifyStart(Screen parent, String levelName, boolean safeMode, boolean canShowBackupPrompt, CallbackInfo ci){
        Modernclassic.LOGGER.info("Overriding canShowBackupPrompt in IntegratedServerLoader start");
        canShowBackupPrompt = false;
    }
/*
    @ModifyVariable(method = "start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V", at = @At(value = "FIELD", target = "canShowBackupPrompt;Z", opcode = Opcodes.PUTFIELD, ordinal = 0), ordinal = 0)
    private boolean modifyCanShowBackupPrompt(boolean original) {
        return false;
    }*/

    @ModifyVariable(method = "start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V", at = @At("HEAD"), ordinal = 1)
    private boolean injectedCanShowBackupPrompt(boolean canShowBackupPrompt) {
        return false;
    }
}