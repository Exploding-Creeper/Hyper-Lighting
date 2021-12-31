package me.hypherionmc.hyperlighting.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColoredWaterSplashParticle extends WaterDropParticle {

    ColoredWaterSplashParticle(ClientLevel world, double x, double y, double z, double sX, double sY, double sZ, float r, float g, float b) {
        super(world, x, y, z);
        this.gravity = 0.04F;
        if (sY == 0.0D && (sX != 0.0D || sZ != 0.0D)) {
            this.xd = sX;
            this.yd = 0.1D;
            this.zd = sZ;
        }

        this.setColor(r, g, b);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        private final float r, g, b;

        public Provider(SpriteSet pSprites, float r, float g, float b) {
            this.sprite = pSprites;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ColoredWaterSplashParticle waterSplashParticle = new ColoredWaterSplashParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, r, g, b);
            waterSplashParticle.pickSprite(this.sprite);
            return waterSplashParticle;
        }
    }
}
