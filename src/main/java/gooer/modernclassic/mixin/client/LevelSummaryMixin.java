package gooer.modernclassic.mixin.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.SaveLoading;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Path;

@Mixin(LevelSummary.class)
public abstract class LevelSummaryMixin{
/*
    @Shadow @Final private LevelInfo levelInfo;

    public LevelSummaryMixin(LevelInfo levelInfo, SaveVersionInfo versionInfo, String name, boolean requiresConversion, boolean locked, Path iconPath) {
        super(levelInfo, versionInfo, name, requiresConversion, locked, iconPath);
    }

    @Shadow public abstract Text getDetails();

    @Shadow public abstract LevelInfo getLevelInfo();

    @Shadow @Final private String name;

    @Overwrite
    public String getName(){

        this.levelInfo.getDataPackSettings().getEnabled();








    }

*/

}