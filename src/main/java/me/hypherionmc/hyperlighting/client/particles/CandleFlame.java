package me.hypherionmc.hyperlighting.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class CandleFlame extends FlameParticle {

    protected CandleFlame(ClientLevel world, double x, double y, double z, double colorR, double colorG, double colorB) {
        super(world, x, y, z, colorR, colorG, colorB);
        this.scale(0.5f);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FACTORY implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FACTORY(SpriteSet spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CandleFlame particle = new CandleFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
