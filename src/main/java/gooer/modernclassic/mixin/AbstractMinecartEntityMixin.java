package gooer.modernclassic.mixin;

import gooer.modernclassic.Modernclassic;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import gooer.modernclassic.entity.vehicle.CustomMinecartTypeEnum;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {


    @Shadow public abstract AbstractMinecartEntity.Type getMinecartType();

    /**
     * @author Max
     * @reason Add SPAWNER MINECART
     */
    @Overwrite
    public ItemStack getPickBlockStack() {
        return new ItemStack(
          switch (this.getMinecartType()) {

              case FURNACE -> Items.FURNACE_MINECART;
              case CHEST -> Items.CHEST_MINECART;
              case TNT -> Items.TNT_MINECART;
              case HOPPER -> Items.HOPPER_MINECART;
              case COMMAND_BLOCK -> Items.COMMAND_BLOCK_MINECART;
              case SPAWNER -> Modernclassic.SPAWNER_MINECART_ITEM;
              default -> Items.MINECART;

          }


        );

    }






}