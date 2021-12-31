package me.hypherionmc.hyperlighting.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColoredWaterBubbleParticle extends TextureSheetParticle {

    ColoredWaterBubbleParticle(ClientLevel world, double x, double y, double z, double d, double e, double f, float r, float g, float b) {
        super(world, x, y, z);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.xd = d * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.yd = e * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.zd = f * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));

        this.setColor(r, g, b);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd += 0.002D;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.85F;
            this.yd *= 0.85F;
            this.zd *= 0.85F;
            if (!this.level.getFluidState(new BlockPos(this.x, this.y, this.z)).is(FluidTags.WATER)) {
                this.remove();
            }

        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
            ColoredWaterBubbleParticle coloredWaterBubble = new ColoredWaterBubbleParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, r, g, b);
            coloredWaterBubble.pickSprite(this.sprite);
            return coloredWaterBubble;
        }
    }
}
