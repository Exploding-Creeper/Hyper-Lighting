package me.hypherionmc.hyperlighting.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

@Environment(value = EnvType.CLIENT)
public class ColoredWaterBubbleParticle extends SpriteBillboardParticle {

    public ColoredWaterBubbleParticle(ClientWorld world, double d, double e, double f, double g, double h, double i, float r, float gg, float b) {
        super(world, d, e, f);
        this.setBoundingBoxSpacing(0.02f, 0.02f);
        this.scale *= this.random.nextFloat() * 0.6f + 0.2f;
        this.velocityX = g * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityY = h * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityZ = i * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.maxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2));

        this.colorRed = r;
        this.colorGreen = gg;
        this.colorBlue = b;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
            return;
        }
        this.velocityY += 0.002;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.85f;
        this.velocityY *= 0.85f;
        this.velocityZ *= 0.85f;
        if (!this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private final float r, gg, b;

        public Factory(SpriteProvider spriteProvider, float r, float g, float b) {
            this.spriteProvider = spriteProvider;
            this.r = r;
            this.gg = g;
            this.b = b;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredWaterBubbleParticle waterBubbleParticle = new ColoredWaterBubbleParticle(clientWorld, d, e, f, g, h, i, r, gg, b);
            waterBubbleParticle.setSprite(this.spriteProvider);
            return waterBubbleParticle;
        }
    }
}
