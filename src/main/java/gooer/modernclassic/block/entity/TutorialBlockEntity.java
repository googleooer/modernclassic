package gooer.modernclassic.block.entity;

import gooer.modernclassic.Modernclassic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TutorialBlockEntity extends BlockEntity {
    //DurationOverride = -1  ->  No override.
    //private int durationOverride = -1;
    private boolean consumable = true;
    private boolean consumed = false;
    private String tutorialId = "modernclassic:empty";

    public TutorialBlockEntity(BlockPos pos, BlockState state) {
        super(Modernclassic.TUTORIAL_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        //nbt.putInt("durationOverride", durationOverride);
        nbt.putBoolean("consumable", consumable);
        nbt.putBoolean("consumed", consumed);
        nbt.putString("id", tutorialId);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        //durationOverride = nbt.getInt("durationOverride");
        consumable = nbt.getBoolean("consumable");
        consumed = nbt.getBoolean("consumed");
        tutorialId = nbt.getString("id");
    }

    //public void setDurationOverride(int durationOverride) {
        //this.durationOverride = durationOverride;
    //}

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }


    //public int getDurationoverride() {
        //return durationOverride;
    //}

    public boolean getConsumable() {
        return consumable;
    }

    public boolean getConsumed() {
        return consumed;
    }

    public String getTutorialId() {
        return tutorialId;
    }


}