package gooer.modernclassic.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class BetaRewindServerState extends PersistentState {

    public boolean ignoreExperimentalWarning = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("ignoreExperimentalWarning", ignoreExperimentalWarning);
        return nbt;
    }

    public static BetaRewindServerState createFromNbt(NbtCompound tag) {
        BetaRewindServerState playerState = new BetaRewindServerState();
        playerState.ignoreExperimentalWarning = tag.getBoolean("totalFurnacesCrafted");
        return playerState;
    }

    public static BetaRewindServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();


        BetaRewindServerState serverState = persistentStateManager.getOrCreate(
                BetaRewindServerState::createFromNbt,
                BetaRewindServerState::new,
                "modernclassic");

        return serverState;
    }
}