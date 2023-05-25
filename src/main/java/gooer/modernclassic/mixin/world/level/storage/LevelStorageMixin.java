package gooer.modernclassic.mixin.world.level.storage;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.duck_accessors.world.level.storage.LevelSummaryAccessor;
import net.minecraft.nbt.NbtElement;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashMemoryReserve;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.SessionLock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Mixin(LevelStorage.class)
public abstract class LevelStorageMixin {
/*
    @Shadow @Final private DataFixer dataFixer;


    @Shadow abstract BiFunction<Path, DataFixer, LevelSummary> createLevelDataParser(LevelStorage.LevelSave levelSave, boolean locked);

    @Shadow abstract BiFunction<Path, DataFixer, LevelProperties> createLevelDataParser(DynamicOps<NbtElement> ops, DataPackSettings dataPackSettings, Lifecycle lifecycle);
*/
    /**
     * @author Max
     * @reason Add lifecycle to LevelSummary
     *//*
    @Overwrite
    public CompletableFuture<List<LevelSummary>> loadSummaries(LevelStorage.LevelList levels) {
        ArrayList<CompletableFuture<LevelSummary>> list = new ArrayList<CompletableFuture<LevelSummary>>(levels.levels().size());
        for (LevelStorage.LevelSave levelSave : levels.levels()) {
            list.add(CompletableFuture.supplyAsync(() -> {
                boolean bl;
                try {
                    bl = SessionLock.isLocked(levelSave.path());
                }
                catch (Exception exception) {
                    Modernclassic.LOGGER.warn("Failed to read {} lock", (Object)levelSave.path(), (Object)exception);
                    return null;
                }
                try {
                    LevelSummary levelSummary = this.readLevelProperties(levelSave, this.createLevelDataParser(levelSave, bl));
                    if (levelSummary != null) {
                        return levelSummary;
                    }
                }
                catch (OutOfMemoryError outOfMemoryError) {
                    CrashMemoryReserve.releaseMemory();
                    System.gc();
                    Modernclassic.LOGGER.error(LogUtils.FATAL_MARKER, "Ran out of memory trying to read summary of {}", (Object)levelSave.getRootPath());
                    throw outOfMemoryError;
                }
                catch (StackOverflowError stackOverflowError) {
                    Modernclassic.LOGGER.error(LogUtils.FATAL_MARKER, "Ran out of stack trying to read summary of {}. Assuming corruption; attempting to restore from from level.dat_old.", (Object)levelSave.getRootPath());
                    Util.backupAndReplace(levelSave.getLevelDatPath(), levelSave.getLevelDatOldPath(), levelSave.getCorruptedLevelDatPath(LocalDateTime.now()), true);
                    throw stackOverflowError;
                }
                return null;
            }, Util.getMainWorkerExecutor()));
        }
        return Util.combineCancellable(list).thenApply(summaries -> summaries.stream().filter(Objects::nonNull).sorted().toList());
    }
*/







    /**
     * @author Max
     * @reason Implement lifecycle into LevelSummary
     *//*
    @Overwrite
    @Nullable
    <T> T readLevelProperties(LevelStorage.LevelSave levelSave, BiFunction<Path, DataFixer, T> levelDataParser) {
        T object;
        if (!Files.exists(levelSave.path(), new LinkOption[0])) {
            return null;
        }
        Path path = levelSave.getLevelDatPath();
        if (Files.exists(path, new LinkOption[0]) && (object = levelDataParser.apply(path, this.dataFixer)) != null) {
            return object;
        }
        path = levelSave.getLevelDatOldPath();
        if (Files.exists(path, new LinkOption[0])) {
            return levelDataParser.apply(path, this.dataFixer);
        }
        return null;
    }*/

}