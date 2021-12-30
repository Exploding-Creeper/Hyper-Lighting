package me.hypherionmc.hyperlighting.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(value= EnvType.CLIENT)
public class ColoredWaterSplashParticle extends RainSplashParticle {

    ColoredWaterSplashParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, float r, float gg, float b) {
        super(clientWorld, d, e, f);
        this.gravityStrength = 0.04f;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.velocityX = g;
            this.velocityY = 0.1;
            this.velocityZ = i;
        }
        this.colorRed = r;
        this.colorGreen = gg;
        this.colorBlue = b;
    }

    @Environment(value=EnvType.CLIENT)
    public static class SplashFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private final float r, gg, b;

        public SplashFactory(SpriteProvider spriteProvider, float r, float g, float b) {
            this.spriteProvider = spriteProvider;
            this.r = r;
            this.gg = g;
            this.b = b;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredWaterSplashParticle waterSplashParticle = new ColoredWaterSplashParticle(clientWorld, d, e, f, g, h, i, r, gg, b);
            waterSplashParticle.setSprite(this.spriteProvider);
            return waterSplashParticle;
        }
    }

}
