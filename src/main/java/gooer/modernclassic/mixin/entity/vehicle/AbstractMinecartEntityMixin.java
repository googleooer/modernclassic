package gooer.modernclassic.mixin.entity.vehicle;

import com.mojang.datafixers.util.Pair;
import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.block.PoweredCopperRailBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    private static final double MAX_SPEED = 24.0f/20.0f;
    private static final double VANILLA_MAX_SPEED = 8.0f/20.0f;
    private static final double SQRT_TWO = 1.414213;

    private Vec3d lastPos = null;
    private double speedCap = MAX_SPEED;


    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author Max
     * @reason Increase max minecart speed from 8 to 16
     */
    @Overwrite
    public double getMaxSpeed() {

        return (this.isTouchingWater() ? 8.0 / 20.0 : getAdjustedMaxSpeed());

    }

    @Redirect(method = "moveOnRail", at = @At(value = "INVOKE", ordinal = 0, target = "java/lang/Math.min(DD)D"))
    public double speedClamp(double d1, double d2) {
        final double maxSpeed = getAdjustedMaxSpeed();
        return maxSpeed == VANILLA_MAX_SPEED ? Math.min(d1, d2) // i.e. preserve vanilla behavior
                : Math.min(maxSpeed * SQRT_TWO, d2);
    }

    /**
     * @author Max
     * @reason Add support for copper rails
     */
    @Overwrite
    public void moveOnRail(BlockPos pos, BlockState state) {
        double w;
        Vec3d vec3d5;
        double u;
        double t;
        double s;
        this.onLanding();
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        Vec3d vec3d = ((AbstractMinecartEntity)(Object)this).snapPositionToRail(d, e, f);
        e = pos.getY();
        boolean bl = false;
        boolean bl2 = false;
        if (state.isOf(Blocks.POWERED_RAIL) || state.isOf(Modernclassic.POWERED_COPPER_RAIL)) {
            bl = state.get(PoweredRailBlock.POWERED) || state.get(PoweredCopperRailBlock.POWERED);
            bl2 = !bl;
        }
        double g = 0.0078125;
        if (this.isTouchingWater()) {
            g *= 0.2;
        }
        Vec3d vec3d2 = this.getVelocity();
        RailShape railShape = state.get(((AbstractRailBlock)state.getBlock()).getShapeProperty());
        switch (railShape) {
            case ASCENDING_EAST: {
                this.setVelocity(vec3d2.add(-g, 0.0, 0.0));
                e += 1.0;
                break;
            }
            case ASCENDING_WEST: {
                this.setVelocity(vec3d2.add(g, 0.0, 0.0));
                e += 1.0;
                break;
            }
            case ASCENDING_NORTH: {
                this.setVelocity(vec3d2.add(0.0, 0.0, g));
                e += 1.0;
                break;
            }
            case ASCENDING_SOUTH: {
                this.setVelocity(vec3d2.add(0.0, 0.0, -g));
                e += 1.0;
            }
        }
        vec3d2 = this.getVelocity();
        Pair<Vec3i, Vec3i> pair = AbstractMinecartEntity.getAdjacentRailPositionsByShape(railShape);
        Vec3i vec3i = pair.getFirst();
        Vec3i vec3i2 = pair.getSecond();
        double h = vec3i2.getX() - vec3i.getX();
        double i = vec3i2.getZ() - vec3i.getZ();
        double j = Math.sqrt(h * h + i * i);
        double k = vec3d2.x * h + vec3d2.z * i;
        if (k < 0.0) {
            h = -h;
            i = -i;
        }
        double l = Math.min(2.0, vec3d2.horizontalLength());
        vec3d2 = new Vec3d(l * h / j, vec3d2.y, l * i / j);
        this.setVelocity(vec3d2);
        Entity entity = this.getFirstPassenger();
        if (entity instanceof PlayerEntity) {
            Vec3d vec3d3 = entity.getVelocity();
            double m = vec3d3.horizontalLengthSquared();
            double n = this.getVelocity().horizontalLengthSquared();
            if (m > 1.0E-4 && n < 0.01) {
                this.setVelocity(this.getVelocity().add(vec3d3.x * 0.1, 0.0, vec3d3.z * 0.1));
                bl2 = false;
            }
        }
        if (bl2) {
            double o = this.getVelocity().horizontalLength();
            if (o < 0.03) {
                this.setVelocity(Vec3d.ZERO);
            } else {
                this.setVelocity(this.getVelocity().multiply(0.5, 0.0, 0.5));
            }
        }
        double o = (double)pos.getX() + 0.5 + (double)vec3i.getX() * 0.5;
        double p = (double)pos.getZ() + 0.5 + (double)vec3i.getZ() * 0.5;
        double q = (double)pos.getX() + 0.5 + (double)vec3i2.getX() * 0.5;
        double r = (double)pos.getZ() + 0.5 + (double)vec3i2.getZ() * 0.5;
        h = q - o;
        i = r - p;
        if (h == 0.0) {
            s = f - (double)pos.getZ();
        } else if (i == 0.0) {
            s = d - (double)pos.getX();
        } else {
            t = d - o;
            u = f - p;
            s = (t * h + u * i) * 2.0;
        }
        d = o + h * s;
        f = p + i * s;
        this.setPosition(d, e, f);
        t = this.hasPassengers() ? 0.75 : 1.0;
        u = this.getMaxSpeed();
        vec3d2 = this.getVelocity();
        this.move(MovementType.SELF, new Vec3d(MathHelper.clamp(t * vec3d2.x, -u, u), 0.0, MathHelper.clamp(t * vec3d2.z, -u, u)));
        if (vec3i.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i.getZ()) {
            this.setPosition(this.getX(), this.getY() + (double)vec3i.getY(), this.getZ());
        } else if (vec3i2.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i2.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i2.getZ()) {
            this.setPosition(this.getX(), this.getY() + (double)vec3i2.getY(), this.getZ());
        }
        ((AbstractMinecartEntity)(Object)this).applySlowdown();
        Vec3d vec3d4 = ((AbstractMinecartEntity)(Object)this).snapPositionToRail(this.getX(), this.getY(), this.getZ());
        if (vec3d4 != null && vec3d != null) {
            double v = (vec3d.y - vec3d4.y) * 0.05;
            vec3d5 = this.getVelocity();
            w = vec3d5.horizontalLength();
            if (w > 0.0) {
                this.setVelocity(vec3d5.multiply((w + v) / w, 1.0, (w + v) / w));
            }
            this.setPosition(this.getX(), vec3d4.y, this.getZ());
        }
        int x = MathHelper.floor(this.getX());
        int y = MathHelper.floor(this.getZ());
        if (x != pos.getX() || y != pos.getZ()) {
            vec3d5 = this.getVelocity();
            w = vec3d5.horizontalLength();
            this.setVelocity(w * (double)(x - pos.getX()), vec3d5.y, w * (double)(y - pos.getZ()));
        }
        if (bl) {

            if(state.isOf(Blocks.POWERED_RAIL)) {


                vec3d5 = this.getVelocity();
                w = vec3d5.horizontalLength();
                if (w > 0.01) {
                    double z = 0.06;
                    this.setVelocity(vec3d5.add(vec3d5.x / w * 0.06, 0.0, vec3d5.z / w * 0.06));
                } else {
                    Vec3d vec3d6 = this.getVelocity();
                    double aa = vec3d6.x;
                    double ab = vec3d6.z;
                    if (railShape == RailShape.EAST_WEST) {
                        if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.west())) {
                            aa = 0.02;
                        } else if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.east())) {
                            aa = -0.02;
                        }
                    } else if (railShape == RailShape.NORTH_SOUTH) {
                        if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.north())) {
                            ab = 0.02;
                        } else if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.south())) {
                            ab = -0.02;
                        }
                    } else {
                        return;
                    }
                    this.setVelocity(aa, vec3d6.y, ab);
                }

            } else if(state.isOf(Modernclassic.POWERED_COPPER_RAIL)){



                vec3d5 = this.getVelocity();
                w = vec3d5.horizontalLength();

                if (w > 0.01) {


                        Vec3d newSpeed = vec3d5.add(vec3d5.x / w * 0.06, 0.0, vec3d5.z / w * 0.06);

                        double z = 0.06;
                        this.setVelocity(
                                MathHelper.clamp(newSpeed.x, -1.0f, 1.0f),
                                newSpeed.y,
                                MathHelper.clamp(newSpeed.z, -1.0f, 1.0f)
                        );



                    } else {
                        Vec3d vec3d6 = this.getVelocity();
                        double aa = vec3d6.x;
                        double ab = vec3d6.z;
                        if (railShape == RailShape.EAST_WEST) {
                            if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.west())) {
                                aa = 0.02;
                            } else if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.east())) {
                                aa = -0.02;
                            }
                        } else if (railShape == RailShape.NORTH_SOUTH) {
                            if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.north())) {
                                ab = 0.02;
                            } else if (((AbstractMinecartEntity) (Object) this).willHitBlockAt(pos.south())) {
                                ab = -0.02;
                            }
                        } else {
                            return;
                        }
                        this.setVelocity(aa, vec3d6.y, ab);
                    }




            }
        }
    }


    private double getAdjustedMaxSpeed() {

        //Thanks pcal43 for the open-source code!

        final BlockPos pos = this.getBlockPos();
        if (pos.equals(lastPos)) return speedCap;
        lastPos = Vec3d.ofCenter(pos);

        final Vec3d speed = this.getVelocity();
        final BlockPos nextPos = new BlockPos(
                pos.getX() + MathHelper.sign(speed.getX()),
                pos.getY(),
                pos.getZ() + MathHelper.sign(speed.getZ())
        );
        final BlockState nextState = this.world.getBlockState(nextPos);

        if(nextState.getBlock() instanceof AbstractRailBlock rail) {
            final RailShape shape = nextState.get(rail.getShapeProperty());
            if (shape == RailShape.NORTH_EAST || shape == RailShape.NORTH_WEST || shape == RailShape.SOUTH_EAST || shape == RailShape.SOUTH_WEST) {
                return speedCap = VANILLA_MAX_SPEED;
            } else {
                final BlockState underState = this.world.getBlockState(pos.down());
                final Identifier underBlockId = Registry.BLOCK.getId(underState.getBlock());

                return speedCap = MAX_SPEED;
            }

        } else {
            return speedCap = VANILLA_MAX_SPEED;
        }


    }

}