package me.hypherionmc.hyperlighting.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class FogMachineSmoke extends TextureSheetParticle {

    Random rand = new Random();
    protected final int halfMaxAge;
    protected final float alphaStep;
    private final float rotIncrement;
    private final Vec3 move;
    private final SpriteSet spriteSet;

    public FogMachineSmoke(SpriteSet spriteSet, ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, DyeColor color) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.xd = xSpeedIn;
        this.yd = ySpeedIn;
        this.zd = zSpeedIn;
        this.alpha = 0.11F;
        this.scale(4F);
        this.rotIncrement = (float)(3.141592653589793D * (double)worldIn.random.nextFloat() * 0.005D) * (float)(rand.nextBoolean() ? -1 : 1);
        this.setLifetime(50);
        this.halfMaxAge = this.lifetime / 2;
        this.alphaStep = 0.03F;
        this.hasPhysics = true;
        this.speedUpWhenYMotionIsBlocked = false;
        this.move = new Vec3((double)(random.nextFloat() - random.nextFloat()) / 200.0D, 0.0D, (double)(random.nextFloat() - random.nextFloat()) / 200.0D);
        this.setColor(color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);

        this.spriteSet = spriteSet;
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha(Mth.clamp(this.age < this.halfMaxAge ? this.age : this.lifetime - this.age, 0, this.halfMaxAge) * this.alphaStep);
    }

    @Override
    public int getLightColor(float partialTick) {
        int skylight = 8;
        int blocklight = 15;
        return skylight << 20 | blocklight << 4;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        RenderSystem.depthMask(false);
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        private final DyeColor color;

        public Provider(SpriteSet spriteSetIn, DyeColor color) {
            this.spriteSet = spriteSetIn;
            this.color = color;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FogMachineSmoke(spriteSet, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, color);
        }
    }
}
