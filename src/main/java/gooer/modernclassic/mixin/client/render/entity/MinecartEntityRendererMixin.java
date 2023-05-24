package gooer.modernclassic.mixin.client.render.entity;

import gooer.modernclassic.util.ModernclassicUtils;
import net.minecraft.block.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecartEntityRenderer.class)
public class MinecartEntityRendererMixin<T extends AbstractMinecartEntity>
extends EntityRenderer<T> {


    @Shadow @Final private static Identifier TEXTURE;

    @Shadow protected final EntityModel<T> model;
    @Shadow private final BlockRenderManager blockRenderManager;

    public MinecartEntityRendererMixin(EntityRendererFactory.Context ctx, EntityModelLayer layer) {
        super(ctx);
        this.shadowRadius = 0.7f;
        this.model = new MinecartEntityModel(ctx.getPart(layer));
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    /**
     * @author Max
     * @reason Fix minecart block rotation
     */
    @Overwrite
    public void render(T abstractMinecartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(abstractMinecartEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        long l = (long)((Entity)abstractMinecartEntity).getId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float)(l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float j = (((float)(l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float k = (((float)(l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        matrixStack.translate(h, j, k);
        double d = MathHelper.lerp((double)g, ((AbstractMinecartEntity)abstractMinecartEntity).lastRenderX, ((Entity)abstractMinecartEntity).getX());
        double e = MathHelper.lerp((double)g, ((AbstractMinecartEntity)abstractMinecartEntity).lastRenderY, ((Entity)abstractMinecartEntity).getY());
        double m = MathHelper.lerp((double)g, ((AbstractMinecartEntity)abstractMinecartEntity).lastRenderZ, ((Entity)abstractMinecartEntity).getZ());
        double n = 0.3f;
        Vec3d vec3d = ((AbstractMinecartEntity)abstractMinecartEntity).snapPositionToRail(d, e, m);
        float o = MathHelper.lerp(g, ((AbstractMinecartEntity)abstractMinecartEntity).prevPitch, ((Entity)abstractMinecartEntity).getPitch());
        if (vec3d != null) {
            Vec3d vec3d2 = ((AbstractMinecartEntity)abstractMinecartEntity).snapPositionToRailWithOffset(d, e, m, 0.3f);
            Vec3d vec3d3 = ((AbstractMinecartEntity)abstractMinecartEntity).snapPositionToRailWithOffset(d, e, m, -0.3f);
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }
            if (vec3d3 == null) {
                vec3d3 = vec3d;
            }
            matrixStack.translate(vec3d.x - d, (vec3d2.y + vec3d3.y) / 2.0 - e, vec3d.z - m);
            Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0) {
                vec3d4 = vec3d4.normalize();
                f = (float)(Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
                o = (float)(Math.atan(vec3d4.y) * 73.0);
            }
        }
        matrixStack.translate(0.0, 0.375, 0.0);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-o));
        float p = (float)((AbstractMinecartEntity)abstractMinecartEntity).getDamageWobbleTicks() - g;
        float q = ((AbstractMinecartEntity)abstractMinecartEntity).getDamageWobbleStrength() - g;
        if (q < 0.0f) {
            q = 0.0f;
        }
        if (p > 0.0f) {
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(p) * p * q / 10.0f * (float)((AbstractMinecartEntity)abstractMinecartEntity).getDamageWobbleSide()));
        }
        int r = ((AbstractMinecartEntity)abstractMinecartEntity).getBlockOffset();
        BlockState blockState = ((AbstractMinecartEntity)abstractMinecartEntity).getContainedBlock();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            matrixStack.push();
            float s = 0.75f;

            //=if(abstractMinecartEntity.getContainedBlock().getProperties().contains(FacingBlock.FACING)){
                //Direction blockDirection = abstractMinecartEntity.getContainedBlock().get(FacingBlock.FACING);
                //Direction cartDirection = abstractMinecartEntity.getMovementDirection();
                Direction cartDirection = ModernclassicUtils.minecartDirectionToVelocity(abstractMinecartEntity);


                switch (cartDirection) {
                    case NORTH:
                        //matrixStack.translate(-0.5, (float)(r - 8) / 16.0f, 0.5);
                        //matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
                        if(blockState.getProperties().contains(FacingBlock.FACING)) blockState = blockState.with(FacingBlock.FACING,Direction.NORTH);
                        if(blockState.getProperties().contains(FurnaceBlock.FACING)) blockState = blockState.with(FurnaceBlock.FACING,Direction.NORTH);
                        if(blockState.getProperties().contains(ChestBlock.FACING)) blockState = blockState.with(ChestBlock.FACING,Direction.NORTH);
                        break;
                    case SOUTH:
                        //matrixStack.translate(0.5, (float)(r - 8) / 16.0f, -0.5);
                        //matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0f));
                        if(blockState.getProperties().contains(FacingBlock.FACING)) blockState = blockState.with(FacingBlock.FACING,Direction.SOUTH);
                        if(blockState.getProperties().contains(FurnaceBlock.FACING)) blockState = blockState.with(FurnaceBlock.FACING,Direction.SOUTH);
                        if(blockState.getProperties().contains(ChestBlock.FACING)) blockState = blockState.with(ChestBlock.FACING,Direction.SOUTH);
                        break;
                    case EAST:
                        //matrixStack.translate(0.5, (float)(r - 8) / 16.0f, 0.5);
                        //matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
                        if(blockState.getProperties().contains(FacingBlock.FACING)) blockState = blockState.with(FacingBlock.FACING,Direction.SOUTH);
                        if(blockState.getProperties().contains(FurnaceBlock.FACING)) blockState = blockState.with(FurnaceBlock.FACING,Direction.SOUTH);
                        if(blockState.getProperties().contains(ChestBlock.FACING)) blockState = blockState.with(ChestBlock.FACING,Direction.EAST);
                        break;
                    case WEST:
                        //matrixStack.scale(-1.0f,1.0f,1.0f);
                        //matrixStack.translate(0.5, (float)(r - 8) / 16.0f, 0.5);
                        //matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
                        if(blockState.getProperties().contains(FacingBlock.FACING)) blockState = blockState.with(FacingBlock.FACING,Direction.NORTH);
                        if(blockState.getProperties().contains(FurnaceBlock.FACING)) blockState = blockState.with(FurnaceBlock.FACING,Direction.NORTH);
                        if(blockState.getProperties().contains(ChestBlock.FACING)) blockState = blockState.with(ChestBlock.FACING,Direction.WEST);
                        break;
                }
                //LoggerFactory.getLogger("ModernClassic").info("Minecart movement direction: " + cartDirection +
                        //", Flipped?: " + abstractMinecartEntity.yawFlipped +
                        //", Moving normal: " + abstractMinecartEntity.getVelocity().normalize());



            //}

            matrixStack.scale(0.75f, 0.75f, 0.75f);
            matrixStack.translate(-0.5, (float)(r - 8) / 16.0f, 0.5);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));




            this.renderBlock(abstractMinecartEntity, g, blockState, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        this.model.setAngles(abstractMinecartEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(abstractMinecartEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }

    public void renderBlock(T entity, float delta, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {


        this.blockRenderManager.renderBlockAsEntity(state, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);



    }

    /*
    public Direction getMovementDirectionReal(AbstractMinecartEntity minecart){
        return minecart.getHorizontalFacing().getOpposite().rotateYClockwise();

        return minecart.getVelocity().normalize();



    }

     */

}