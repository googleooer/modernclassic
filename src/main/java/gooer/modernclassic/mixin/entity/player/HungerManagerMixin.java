package gooer.modernclassic.mixin.entity.player;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {


    @Shadow private int foodLevel;

    @Shadow private float exhaustion;

    @Shadow private float saturationLevel;

    @Shadow private int foodTickTimer;

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Shadow private int prevFoodLevel;

    private PlayerEntity owningPlayer;

    /**
     * @author Max
     * @reason Rework hunger
     */
    @Overwrite
    public void update(PlayerEntity player) {
        owningPlayer = player;
        boolean bl;
        Difficulty difficulty = player.world.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.exhaustion > 4.0f) {
            this.exhaustion -= 4.0f;
            if (this.saturationLevel > 0.0f) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0f, 0.0f);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if ((bl = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) /*&& this.saturationLevel > 0.0f */&& player.canFoodHeal() && this.foodLevel >= 4) {
            ++this.foodTickTimer;

            if (this.foodTickTimer >= (int)(2100*((double)2/this.foodLevel)) ){
            //if (this.foodTickTimer >= 2100/this.foodLevel) {
                float f = Math.min(this.saturationLevel, 6.0f);
                player.heal(1.0f);
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
                this.foodTickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                if (player.getHealth() > 10.0f || difficulty == Difficulty.HARD || player.getHealth() > 1.0f && difficulty == Difficulty.NORMAL) {
                    player.damage(DamageSource.STARVE, 1.0f);
                }
                this.foodTickTimer = 0;
            }
        } else {
            this.foodTickTimer = 0;
        }
    }




    /**
     * @author Max
     * @reason Make food add health
     */
    @Overwrite
    public void add(int food, float saturationModifier) {
        this.foodLevel = Math.min((int)Math.floor(food*0.8) + this.foodLevel, 20);
        this.saturationLevel = Math.min(this.saturationLevel + (float)food * saturationModifier * 2.0f, (float)this.foodLevel);
        if(owningPlayer != null) owningPlayer.heal((int)(food*0.6));
    }




}