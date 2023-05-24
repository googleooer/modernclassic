package gooer.modernclassic.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {


    @Shadow private static float blue;
    @Shadow private static long lastWaterFogColorUpdateTime;
    @Shadow private static int waterFogColor;
    @Shadow private static int nextWaterFogColor;
    @Shadow private static float red;
    @Shadow private static float green;
    private MinecraftClient client;


    @Unique @Nullable private VertexBuffer blueBuffer;


    @Unique
    private void createBlueBuffer(){
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder builder = tesselator.getBuffer();

        if(this.blueBuffer != null) this.blueBuffer.close();

        float height = -48.0f;

        this.blueBuffer = new VertexBuffer();
        BufferBuilder.BuiltBuffer renderedBuffer = buildSkyDisc(builder, height);
        this.blueBuffer.bind();
        this.blueBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();

    }


    private static BufferBuilder.BuiltBuffer buildSkyDisc(BufferBuilder builder, float y){
        float x = Math.signum(y) * 512.0f;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        builder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        builder.vertex(0.0,y,0.0).next();

        for(int i = -180; i <= 180; i += 45) builder.vertex(x * Math.cos((float) i * ((float) Math.PI / 180)), y, 512.0F * Math.sin((float) i * ((float) Math.PI / 180))).next();

        return builder.end();
    }


    @ModifyVariable(
            method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V",
            at = @At(value = "STORE",ordinal = 1),
            name = "r"
    )
    private static float renderOverride(float value) {
        return 8 / 32.0f;
    }

    /**
     * @author Max
     * @reason Overwrite rendering so i can do whatever i want easier
     */
    @Overwrite
    public static void render(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            long l = Util.getMeasuringTimeMs();
            int i = world.getBiome(new BlockPos(camera.getPos())).value().getWaterFogColor();
            if (lastWaterFogColorUpdateTime < 0L) {
                waterFogColor = i;
                nextWaterFogColor = i;
                lastWaterFogColorUpdateTime = l;
            }
            int j = waterFogColor >> 16 & 0xFF;
            int k = waterFogColor >> 8 & 0xFF;
            int m = waterFogColor & 0xFF;
            int n = nextWaterFogColor >> 16 & 0xFF;
            int o = nextWaterFogColor >> 8 & 0xFF;
            int p = nextWaterFogColor & 0xFF;
            float f = MathHelper.clamp((float)(l - lastWaterFogColorUpdateTime) / 5000.0f, 0.0f, 1.0f);
            float g = MathHelper.lerp(f, n, j);
            float h = MathHelper.lerp(f, o, k);
            float q = MathHelper.lerp(f, p, m);
            red = g / 255.0f;
            green = h / 255.0f;
            blue = q / 255.0f;
            if (waterFogColor != i) {
                waterFogColor = i;
                nextWaterFogColor = MathHelper.floor(g) << 16 | MathHelper.floor(h) << 8 | MathHelper.floor(q);
                lastWaterFogColorUpdateTime = l;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            red = 0.6f;
            green = 0.1f;
            blue = 0.0f;
            lastWaterFogColorUpdateTime = -1L;
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            red = 0.623f;
            green = 0.734f;
            blue = 0.785f;
            lastWaterFogColorUpdateTime = -1L;
            RenderSystem.clearColor(red, green, blue, 0.0f);
        } else {
            float g;
            float h;
            float f;
            float r = 0.25f + 0.75f * (float)viewDistance / 32.0f;
            r = 1.0f - (float)Math.pow(r, 0.25);
            Vec3d vec3d = world.getSkyColor(camera.getPos(), tickDelta);
            float s = (float)vec3d.x;
            float t = (float)vec3d.y;
            float u = (float)vec3d.z;
            float v = MathHelper.clamp(MathHelper.cos(world.getSkyAngle(tickDelta) * ((float)Math.PI * 2)) * 2.0f + 0.5f, 0.0f, 1.0f);
            BiomeAccess biomeAccess = world.getBiomeAccess();
            Vec3d vec3d2 = camera.getPos().subtract(2.0, 2.0, 2.0).multiply(0.25);
            Vec3d vec3d3 = CubicSampler.sampleColor(vec3d2, (x, y, z) -> world.getDimensionEffects().adjustFogColor(Vec3d.unpackRgb(biomeAccess.getBiomeForNoiseGen(x, y, z).value().getFogColor()), v));
            red = (float)vec3d3.getX();
            green = (float)vec3d3.getY();
            blue = (float)vec3d3.getZ();
            if (viewDistance >= 4) {
                float[] fs;
                f = 0.0f;
                Vec3f vec3f = new Vec3f(f, 0.0f, 0.0f);
                h = camera.getHorizontalPlane().dot(vec3f);
                if (h < 0.0f) {
                    h = 0.0f;
                }
                if (h > 0.0f && (fs = world.getDimensionEffects().getFogColorOverride(world.getSkyAngle(tickDelta), tickDelta)) != null) {
                    red = red * (1.0f - (h *= fs[3])) + fs[0] * h;
                    green = green * (1.0f - h) + fs[1] * h;
                    blue = blue * (1.0f - h) + fs[2] * h;
                }
            }
            red += (s - red) * r;
            green += (t - green) * r;
            blue += (u - blue) * r;
            f = world.getRainGradient(tickDelta);
            if (f > 0.0f) {
                float g2 = 1.0f - f * 0.5f;
                h = 1.0f - f * 0.4f;
                red *= g2;
                green *= g2;
                blue *= h;
            }
            if ((g = world.getThunderGradient(tickDelta)) > 0.0f) {
                h = 1.0f - g * 0.5f;
                red *= h;
                green *= h;
                blue *= h;
            }
            lastWaterFogColorUpdateTime = -1L;
        }
        float r = ((float)camera.getPos().y - (float)world.getBottomY()) * world.getLevelProperties().getHorizonShadingRatio();
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = BackgroundRenderer.getFogModifier(entity, tickDelta);
        if (statusEffectFogModifier != null) {
            LivingEntity livingEntity = (LivingEntity)entity;
            r = statusEffectFogModifier.applyColorModifier(livingEntity, livingEntity.getStatusEffect(statusEffectFogModifier.getStatusEffect()), r, tickDelta);
        }
        if (r < 1.0f && cameraSubmersionType != CameraSubmersionType.LAVA && cameraSubmersionType != CameraSubmersionType.POWDER_SNOW) {
            if (r < 0.0f) {
                r = 0.0f;
            }
            r *= r;
            red *= r;
            green *= r;
            blue *= r;
        }
        if (skyDarkness > 0.0f) {
            red = red * (1.0f - skyDarkness) + red * 0.7f * skyDarkness;
            green = green * (1.0f - skyDarkness) + green * 0.6f * skyDarkness;
            blue = blue * (1.0f - skyDarkness) + blue * 0.6f * skyDarkness;
        }
        float s = cameraSubmersionType == CameraSubmersionType.WATER ? (entity instanceof ClientPlayerEntity ? ((ClientPlayerEntity)entity).getUnderwaterVisibility() : 1.0f) : (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.NIGHT_VISION) ? GameRenderer.getNightVisionStrength((LivingEntity)entity, tickDelta) : 0.0f);
        if (red != 0.0f && green != 0.0f && blue != 0.0f) {
            float t = Math.min(1.0f / red, Math.min(1.0f / green, 1.0f / blue));
            red = red * (1.0f - s) + red * t * s;
            green = green * (1.0f - s) + green * t * s;
            blue = blue * (1.0f - s) + blue * t * s;
        }
        RenderSystem.clearColor(red, green, blue, 0.0f);
    }




    @Inject(method = "applyFog", at=@At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.setShaderFogEnd(F)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void FogFalloff(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, CameraSubmersionType cameraSubmersionType, Entity entity){

        if (fogType == BackgroundRenderer.FogType.FOG_SKY){
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogStart(0.0f);
            RenderSystem.setShaderFogEnd(viewDistance);
        } else {
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogStart(viewDistance * 0.25f);
            RenderSystem.setShaderFogEnd(viewDistance);
            float brightnessMult = 0.0f;
            switch (cameraSubmersionType){
                case WATER:
                    if (entity instanceof LivingEntity){
                        brightnessMult = ((float)EnchantmentHelper.getRespiration((LivingEntity)entity)-1) * 0.2f;

                        if(((LivingEntity) entity).hasStatusEffect(StatusEffects.WATER_BREATHING)) brightnessMult = brightnessMult * 0.3f + 0.6f;

                    }
                    //RenderSystem.
                    RenderSystem.setShaderFogColor(0.02f + brightnessMult, 0.02f + brightnessMult, 0.2f + brightnessMult);
                    break;
                case LAVA:
                    RenderSystem.setShaderFogColor(0.6f, 0.1f, 0.0f);
                    break;
            }




        }


    }



}