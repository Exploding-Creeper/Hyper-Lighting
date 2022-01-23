package me.hypherionmc.hyperlighting.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class FogMachineSmoke extends SpriteBillboardParticle {

    protected final int halfMaxAge;
    protected final float alphaStep;
    private final float rotIncrement;
    private final Vec3d move;
    private final SpriteProvider spriteSet;
    Random rand = new Random();

    public FogMachineSmoke(SpriteProvider spriteSet, ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, DyeColor color) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.velocityX = xSpeedIn;
        this.velocityY = ySpeedIn;
        this.velocityZ = zSpeedIn;
        this.colorAlpha = 0.11F;
        this.scale(4F);
        this.rotIncrement = (float) (3.141592653589793D * (double) worldIn.random.nextFloat() * 0.005D) * (float) (rand.nextBoolean() ? -1 : 1);
        this.setMaxAge(50);
        this.halfMaxAge = this.maxAge / 2;
        this.alphaStep = 0.03F;
        this.collidesWithWorld = true;
        //this. = false;
        this.move = new Vec3d((double) (random.nextFloat() - random.nextFloat()) / 200.0D, 0.0D, (double) (random.nextFloat() - random.nextFloat()) / 200.0D);
        this.setColor(color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);

        this.spriteSet = spriteSet;
        this.setSpriteForAge(this.spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        setColorAlpha(MathHelper.clamp(this.age < this.halfMaxAge ? this.age : this.maxAge - this.age, 0, this.halfMaxAge) * this.alphaStep);
    }

    @Override
    public int getBrightness(float partialTick) {
        int skylight = 8;
        int blocklight = 15;
        return skylight << 20 | blocklight << 4;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    /*@Override
    public boolean shouldCull() {
        return false;
    }*/

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        RenderSystem.depthMask(false);
        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteSet;
        private final DyeColor color;

        public Provider(SpriteProvider spriteSetIn, DyeColor color) {
            this.spriteSet = spriteSetIn;
            this.color = color;
        }

        @Override
        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FogMachineSmoke(spriteSet, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, color);
        }
    }
}
