package gooer.modernclassic.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import gooer.modernclassic.client.ModernclassicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static net.minecraft.util.math.MathHelper.floor;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow private int ticks;
    @Shadow @Final private float[] field_20795;
    @Shadow @Final private float[] field_20794;
    @Shadow @Final private static Identifier RAIN;
    @Shadow @Final private static Identifier SNOW;
    private MinecraftClient client;


    @Unique @Nullable
    private VertexBuffer blueBuffer;

    //@Shadow protected abstract void renderStars();


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

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at=@At(value = "HEAD"))
    private void cacheSkyPose(MatrixStack matrixStack, Matrix4f projectionMatrix, float partialTicks, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci){
        ModernclassicClient.blueModelView = matrixStack.peek().getPositionMatrix().copy();
        ModernclassicClient.blueProjection = projectionMatrix.copy();
    }

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
    at = @At(
            ordinal = 1,
            shift = At.Shift.AFTER,
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"
    ))
    private void onDrawSkyBuffer(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci){
        setBlueVoidColor();
        Shader shader = RenderSystem.getShader();

        if(this.blueBuffer != null && shader != null){
            this.blueBuffer.bind();
            this.blueBuffer.draw(ModernclassicClient.blueModelView, ModernclassicClient.blueProjection, shader);
            VertexBuffer.unbind();
        }

    }

    @ModifyArg(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
    index = 1,
    at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"
    ))
    private double onTranslateDarkSkyBuffer(double y){
        y = 0.0d;

        return y - Math.max(this.client.gameRenderer.getCamera().getPos().y - 65.0D, 0.0d);
    }

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    ordinal = 2,
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/VertexBuffer;draw(Lnet/minecraft/util/math/Matrix4f;Lnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/Shader;)V"
            )
    )
    private void onRenderDarkVoid(VertexBuffer instance, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Shader shader){
        instance.draw(modelViewMatrix,projectionMatrix,shader);
    }


    /**
     * @author Max
     * @reason Fuck around with rain to see if i can make it not render above clouds
     */
    @Overwrite
    private void renderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        float f = this.client.world.getRainGradient(tickDelta);
        if (f <= 0.0f) {
            return;
        }
        manager.enable();
        ClientWorld world = this.client.world;
        int i = floor(cameraX);

        //float CloudHeight = 112-10;
        //int CloudHeightOffset = (cameraY < CloudHeight) ? 0 : floor(cameraY - CloudHeight)+10;
        //int CloudHeightOffset = cameraY < CloudHeight-10 ? 0 : floor((cameraY - CloudHeight) * MathHelper.getLerpProgress(cameraY,CloudHeight-10, CloudHeight));
        int j = floor(cameraY);
        int k = floor(cameraZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int l = 5;
        if (MinecraftClient.isFancyGraphicsOrBetter()) {
            l = 10;
        }
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        int m = -1;
        float g = (float)ticks + tickDelta;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int n = k - l; n <= k + l; ++n) {
            for (int o = i - l; o <= i + l; ++o) {
                float y;
                float h;
                int t;
                int p = (n - k + 16) * 32 + o - i + 16;
                double d = (double)field_20794[p] * 0.5;
                double e = (double)field_20795[p] * 0.5;
                mutable.set((double)o, cameraY, (double)n);
                Biome biome = world.getBiome(mutable).value();
                if (biome.getPrecipitation() == Biome.Precipitation.NONE) continue;
                int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, o, n);
                int r = j - l;
                int s = j + l;
                if (r < q) {
                    r = q;
                }
                if (s < q) {
                    s = q;
                }
                if ((t = q) < j) {
                    t = j;
                }
                if (r == s) continue;
                Random random = Random.create(o * o * 3121 + o * 45238971 ^ n * n * 418711 + n * 13761);
                mutable.set(o, r, n);
                if (biome.doesNotSnow(mutable)) {
                    if (m != 0) {
                        if (m >= 0) {
                            tessellator.draw();
                        }
                        m = 0;
                        RenderSystem.setShaderTexture(0, RAIN);
                        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                    }
                    int u = this.ticks + o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 0x1F;
                    h = -((float)u + tickDelta) / 32.0f * (3.0f + random.nextFloat());
                    double v = (double)o + 0.5 - cameraX;
                    double w = (double)n + 0.5 - cameraZ;
                    float x = (float)Math.sqrt(v * v + w * w) / (float)l;
                    y = ((1.0f - x * x) * 0.5f + 0.5f) * f;
                    mutable.set(o, t, n);
                    int z = WorldRenderer.getLightmapCoordinates(world, mutable);
                    bufferBuilder.vertex((double)o - cameraX - d + 0.5, (double)s - cameraY, (double)n - cameraZ - e + 0.5).texture(0.0f, (float)r * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                    bufferBuilder.vertex((double)o - cameraX + d + 0.5, (double)s - cameraY, (double)n - cameraZ + e + 0.5).texture(1.0f, (float)r * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                    bufferBuilder.vertex((double)o - cameraX + d + 0.5, (double)r - cameraY, (double)n - cameraZ + e + 0.5).texture(1.0f, (float)s * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                    bufferBuilder.vertex((double)o - cameraX - d + 0.5, (double)r - cameraY, (double)n - cameraZ - e + 0.5).texture(0.0f, (float)s * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                    continue;
                }
                if (m != 1) {
                    if (m >= 0) {
                        tessellator.draw();
                    }
                    m = 1;
                    RenderSystem.setShaderTexture(0, SNOW);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                }
                float aa = -((float)(this.ticks & 0x1FF) + tickDelta) / 512.0f;
                h = (float)(random.nextDouble() + (double)g * 0.01 * (double)((float)random.nextGaussian()));
                float ab = (float)(random.nextDouble() + (double)(g * (float)random.nextGaussian()) * 0.001);
                double ac = (double)o + 0.5 - cameraX;
                double ad = (double)n + 0.5 - cameraZ;
                y = (float)Math.sqrt(ac * ac + ad * ad) / (float)l;
                float ae = ((1.0f - y * y) * 0.3f + 0.5f) * f;
                mutable.set(o, t, n);
                int af = WorldRenderer.getLightmapCoordinates(world, mutable);
                int ag = af >> 16 & 0xFFFF;
                int ah = af & 0xFFFF;
                int ai = (ag * 3 + 240) / 4;
                int aj = (ah * 3 + 240) / 4;
                bufferBuilder.vertex((double)o - cameraX - d + 0.5, (double)s - cameraY, (double)n - cameraZ - e + 0.5).texture(0.0f + h, (float)r * 0.25f + aa + ab).color(1.0f, 1.0f, 1.0f, ae).light(aj, ai).next();
                bufferBuilder.vertex((double)o - cameraX + d + 0.5, (double)s - cameraY, (double)n - cameraZ + e + 0.5).texture(1.0f + h, (float)r * 0.25f + aa + ab).color(1.0f, 1.0f, 1.0f, ae).light(aj, ai).next();
                bufferBuilder.vertex((double)o - cameraX + d + 0.5, (double)r - cameraY, (double)n - cameraZ + e + 0.5).texture(1.0f + h, (float)s * 0.25f + aa + ab).color(1.0f, 1.0f, 1.0f, ae).light(aj, ai).next();
                bufferBuilder.vertex((double)o - cameraX - d + 0.5, (double)r - cameraY, (double)n - cameraZ - e + 0.5).texture(0.0f + h, (float)s * 0.25f + aa + ab).color(1.0f, 1.0f, 1.0f, ae).light(aj, ai).next();
            }
        }
        if (m >= 0) {
            tessellator.draw();
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        manager.disable();
    }




    private static void setBlueVoidColor(){
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientWorld world = minecraftClient.world;
        if(world == null) return;

        float weatherModifier;
        float partialTick = minecraftClient.getTickDelta();
        float timeOfDay = world.getTimeOfDay();
        float boundedTime = (float) (Math.cos(timeOfDay * ((float) Math.PI * 2)) * 2.0f + 0.5f);
        boundedTime = MathHelper.clamp(boundedTime, 0.0f, 1.0f);

        float r = boundedTime;
        float g = boundedTime;
        float b = boundedTime;

        float rainLevel = world.getRainGradient(partialTick);
        float thunderLevel = world.getThunderGradient(partialTick);

        if(rainLevel > 0.0f){
            thunderLevel = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.6F;
            weatherModifier = 1.0F - rainLevel * 0.75F;

            r = r * weatherModifier + thunderLevel * (1.0F - weatherModifier);
            g = g * weatherModifier + thunderLevel * (1.0F - weatherModifier);
            b = b * weatherModifier + thunderLevel * (1.0F - weatherModifier);
        }

        if (thunderLevel > 0.0F)
        {
            float thunderModifier = 1.0F - thunderLevel * 0.75F;
            weatherModifier = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;

            r = r * thunderModifier + weatherModifier * (1.0F - thunderModifier);
            g = g * thunderModifier + weatherModifier * (1.0F - thunderModifier);
            b = b * thunderModifier + weatherModifier * (1.0F - thunderModifier);
        }

        r = MathHelper.clamp(r, 0.1f, 1.0f);
        g = MathHelper.clamp(g, 0.1f, 1.0f);
        b = MathHelper.clamp(b, 0.1f, 1.0f);

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.13f * r, 0.17f * g, 0.7f * b, 1.0f);


    }


}