package me.hypherionmc.hyperlighting.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class CandleFlame extends CustomFlameParticle {

    protected CandleFlame(ClientWorld world, double x, double y, double z, double colorR, double colorG, double colorB) {
        super(world, x, y, z, colorR, colorG, colorB);
        this.scale(0.5f);
    }

    public static class FACTORY implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteSet;

        public FACTORY(SpriteProvider spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Override
        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CandleFlame particle = new CandleFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setSprite(spriteSet);
            return particle;
        }
    }

}
