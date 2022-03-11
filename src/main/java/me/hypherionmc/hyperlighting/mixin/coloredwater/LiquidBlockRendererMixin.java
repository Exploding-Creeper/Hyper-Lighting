package me.hypherionmc.hyperlighting.mixin.coloredwater;

import com.mojang.blaze3d.vertex.VertexConsumer;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.hyperlighting.util.OptiHacks;
import me.hypherionmc.hyperlighting.util.RenderUtils;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Optifine hack around BECAUSE THEY CAN'T STOP FUCKING UP CUSTOM FLUIDS!
 */

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
/*
    @Shadow @Final private TextureAtlasSprite[] lavaIcons;
    @Shadow @Final private TextureAtlasSprite[] waterIcons;
    @Shadow private TextureAtlasSprite waterOverlay;

    @Shadow private static boolean isNeighborSameFluid(BlockGetter pLevel, BlockPos pPos, Direction pSide, FluidState pState) { return false; }
    @Shadow public static boolean shouldRenderFace(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState, BlockState pBlockState, Direction pFace) { return false; }
    @Shadow private float getWaterHeight(BlockGetter pLevel, BlockPos pPos, Fluid pFluid) { return 0f; }
    @Shadow private int getLightColor(BlockAndTintGetter pLevel, BlockPos pPos) { return 0; }
    @Shadow private static boolean isFaceOccludedByNeighbor(BlockGetter pLevel, BlockPos pPos, Direction pFace, float pHeight) { return false; }

    @Inject(at = @At("HEAD"), method = "tesselate", cancellable = true)
    public void tesselate(BlockAndTintGetter pLevel, BlockPos pPos, VertexConsumer pConsumer, FluidState pFluidState, CallbackInfoReturnable<Boolean> cir) {
        // Only apply when fluid is Colored Water and Optifine is present
        if (pFluidState.getType() instanceof ColoredWater coloredWater && OptiHacks.hasOptiFine()) {
            TextureAtlasSprite[] atextureatlassprite = RenderUtils.getFluidTextures(coloredWater);
            BlockState blockstate = pLevel.getBlockState(pPos);
            int i = ModUtils.fluidColorFromDye(coloredWater.getColor());
            float alpha = (float)(i >> 24 & 255) / 255.0F;
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            boolean flag1 = !isNeighborSameFluid(pLevel, pPos, Direction.UP, pFluidState);
            boolean flag2 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate, Direction.DOWN) && !isFaceOccludedByNeighbor(pLevel, pPos, Direction.DOWN, 0.8888889F);
            boolean flag3 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate, Direction.NORTH);
            boolean flag4 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate, Direction.SOUTH);
            boolean flag5 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate, Direction.WEST);
            boolean flag6 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate, Direction.EAST);
            if (!flag1 && !flag2 && !flag6 && !flag5 && !flag3 && !flag4) {
                cir.setReturnValue(false);
            } else {
                boolean flag7 = false;
                float f3 = pLevel.getShade(Direction.DOWN, true);
                float f4 = pLevel.getShade(Direction.UP, true);
                float f5 = pLevel.getShade(Direction.NORTH, true);
                float f6 = pLevel.getShade(Direction.WEST, true);
                float f7 = this.getWaterHeight(pLevel, pPos, pFluidState.getType());
                float f8 = this.getWaterHeight(pLevel, pPos.south(), pFluidState.getType());
                float f9 = this.getWaterHeight(pLevel, pPos.east().south(), pFluidState.getType());
                float f10 = this.getWaterHeight(pLevel, pPos.east(), pFluidState.getType());
                double d0 = pPos.getX() & 15;
                double d1 = pPos.getY() & 15;
                double d2 = pPos.getZ() & 15;
                float f12 = flag2 ? 0.001F : 0.0F;

                if (OptiHacks.isRenderRegions()) {
                    int pj = pPos.getX() >> 4 << 4;
                    int kk = pPos.getY() >> 4 << 4;
                    int ll = pPos.getZ() >> 4 << 4;
                    int ii1 = 8;
                    int j1 = pj >> ii1 << ii1;
                    int k1 = ll >> ii1 << ii1;
                    int l1 = pj - j1;
                    int i2 = ll - k1;
                    d0 += l1;
                    d1 += kk;
                    d2 += i2;
                }

                if (flag1 && !isFaceOccludedByNeighbor(pLevel, pPos, Direction.UP, Math.min(Math.min(f7, f8), Math.min(f9, f10)))) {
                    flag7 = true;
                    f7 -= 0.001F;
                    f8 -= 0.001F;
                    f9 -= 0.001F;
                    f10 -= 0.001F;
                    Vec3 vec3 = pFluidState.getFlow(pLevel, pPos);
                    float f13;
                    float f14;
                    float f15;
                    float f16;
                    float f17;
                    float f18;
                    float f19;
                    float f20;
                    if (vec3.x == 0.0D && vec3.z == 0.0D) {
                        TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
                        f13 = textureatlassprite1.getU(0.0D);
                        f17 = textureatlassprite1.getV(0.0D);
                        f14 = f13;
                        f18 = textureatlassprite1.getV(16.0D);
                        f15 = textureatlassprite1.getU(16.0D);
                        f19 = f18;
                        f16 = f15;
                        f20 = f17;
                    } else {
                        TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
                        float f21 = (float) Mth.atan2(vec3.z, vec3.x) - ((float)Math.PI / 2F);
                        float f22 = Mth.sin(f21) * 0.25F;
                        float f23 = Mth.cos(f21) * 0.25F;
                        float f24 = 8.0F;
                        f13 = textureatlassprite.getU(8.0F + (-f23 - f22) * 16.0F);
                        f17 = textureatlassprite.getV(8.0F + (-f23 + f22) * 16.0F);
                        f14 = textureatlassprite.getU(8.0F + (-f23 + f22) * 16.0F);
                        f18 = textureatlassprite.getV(8.0F + (f23 + f22) * 16.0F);
                        f15 = textureatlassprite.getU(8.0F + (f23 + f22) * 16.0F);
                        f19 = textureatlassprite.getV(8.0F + (f23 - f22) * 16.0F);
                        f16 = textureatlassprite.getU(8.0F + (f23 - f22) * 16.0F);
                        f20 = textureatlassprite.getV(8.0F + (-f23 - f22) * 16.0F);
                    }

                    float f44 = (f13 + f14 + f15 + f16) / 4.0F;
                    float f45 = (f17 + f18 + f19 + f20) / 4.0F;
                    float f46 = (float)atextureatlassprite[0].getWidth() / (atextureatlassprite[0].getU1() - atextureatlassprite[0].getU0());
                    float f47 = (float)atextureatlassprite[0].getHeight() / (atextureatlassprite[0].getV1() - atextureatlassprite[0].getV0());
                    float f48 = 4.0F / Math.max(f47, f46);
                    f13 = Mth.lerp(f48, f13, f44);
                    f14 = Mth.lerp(f48, f14, f44);
                    f15 = Mth.lerp(f48, f15, f44);
                    f16 = Mth.lerp(f48, f16, f44);
                    f17 = Mth.lerp(f48, f17, f45);
                    f18 = Mth.lerp(f48, f18, f45);
                    f19 = Mth.lerp(f48, f19, f45);
                    f20 = Mth.lerp(f48, f20, f45);
                    int j = this.getLightColor(pLevel, pPos);
                    float f25 = f4 * f;
                    float f26 = f4 * f1;
                    float f27 = f4 * f2;
                    this.vertex(pConsumer, d0 + 0.0D, d1 + (double)f7, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
                    this.vertex(pConsumer, d0 + 0.0D, d1 + (double)f8, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
                    this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f9, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
                    this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f10, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
                    if (pFluidState.shouldRenderBackwardUpFace(pLevel, pPos.above())) {
                        this.vertex(pConsumer, d0 + 0.0D, d1 + (double)f7, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
                        this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f10, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
                        this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f9, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
                        this.vertex(pConsumer, d0 + 0.0D, d1 + (double)f8, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
                    }
                }

                if (flag2) {
                    float f35 = atextureatlassprite[0].getU0();
                    float f36 = atextureatlassprite[0].getU1();
                    float f37 = atextureatlassprite[0].getV0();
                    float f39 = atextureatlassprite[0].getV1();
                    int i1 = this.getLightColor(pLevel, pPos.below());
                    float f41 = f3 * f;
                    float f42 = f3 * f1;
                    float f43 = f3 * f2;
                    this.vertex(pConsumer, d0, d1 + (double)f12, d2 + 1.0D, f41, f42, f43, alpha, f35, f39, i1);
                    this.vertex(pConsumer, d0, d1 + (double)f12, d2, f41, f42, f43, alpha, f35, f37, i1);
                    this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f12, d2, f41, f42, f43, alpha, f36, f37, i1);
                    this.vertex(pConsumer, d0 + 1.0D, d1 + (double)f12, d2 + 1.0D, f41, f42, f43, alpha, f36, f39, i1);
                    flag7 = true;
                }

                int k = this.getLightColor(pLevel, pPos);

                for(int l = 0; l < 4; ++l) {
                    float f38;
                    float f40;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    Direction direction;
                    boolean flag8;
                    if (l == 0) {
                        f38 = f7;
                        f40 = f10;
                        d3 = d0;
                        d5 = d0 + 1.0D;
                        d4 = d2 + (double)0.001F;
                        d6 = d2 + (double)0.001F;
                        direction = Direction.NORTH;
                        flag8 = flag3;
                    } else if (l == 1) {
                        f38 = f9;
                        f40 = f8;
                        d3 = d0 + 1.0D;
                        d5 = d0;
                        d4 = d2 + 1.0D - (double)0.001F;
                        d6 = d2 + 1.0D - (double)0.001F;
                        direction = Direction.SOUTH;
                        flag8 = flag4;
                    } else if (l == 2) {
                        f38 = f8;
                        f40 = f7;
                        d3 = d0 + (double)0.001F;
                        d5 = d0 + (double)0.001F;
                        d4 = d2 + 1.0D;
                        d6 = d2;
                        direction = Direction.WEST;
                        flag8 = flag5;
                    } else {
                        f38 = f10;
                        f40 = f9;
                        d3 = d0 + 1.0D - (double)0.001F;
                        d5 = d0 + 1.0D - (double)0.001F;
                        d4 = d2;
                        d6 = d2 + 1.0D;
                        direction = Direction.EAST;
                        flag8 = flag6;
                    }

                    if (flag8 && !isFaceOccludedByNeighbor(pLevel, pPos, direction, Math.max(f38, f40))) {
                        flag7 = true;
                        BlockPos blockpos = pPos.relative(direction);
                        TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
                        if (atextureatlassprite[2] != null) {
                            if (pLevel.getBlockState(blockpos).shouldDisplayFluidOverlay(pLevel, blockpos, pFluidState)) {
                                textureatlassprite2 = atextureatlassprite[2];
                            }
                        }

                        float f49 = textureatlassprite2.getU(0.0D);
                        float f50 = textureatlassprite2.getU(8.0D);
                        float f28 = textureatlassprite2.getV((double)((1.0F - f38) * 16.0F * 0.5F));
                        float f29 = textureatlassprite2.getV((double)((1.0F - f40) * 16.0F * 0.5F));
                        float f30 = textureatlassprite2.getV(8.0D);
                        float f31 = l < 2 ? f5 : f6;
                        float f32 = f4 * f31 * f;
                        float f33 = f4 * f31 * f1;
                        float f34 = f4 * f31 * f2;
                        this.vertex(pConsumer, d3, d1 + (double)f38, d4, f32, f33, f34, alpha, f49, f28, k);
                        this.vertex(pConsumer, d5, d1 + (double)f40, d6, f32, f33, f34, alpha, f50, f29, k);
                        this.vertex(pConsumer, d5, d1 + (double)f12, d6, f32, f33, f34, alpha, f50, f30, k);
                        this.vertex(pConsumer, d3, d1 + (double)f12, d4, f32, f33, f34, alpha, f49, f30, k);
                        if (textureatlassprite2 != this.waterOverlay) {
                            this.vertex(pConsumer, d3, d1 + (double)f12, d4, f32, f33, f34, alpha, f49, f30, k);
                            this.vertex(pConsumer, d5, d1 + (double)f12, d6, f32, f33, f34, alpha, f50, f30, k);
                            this.vertex(pConsumer, d5, d1 + (double)f40, d6, f32, f33, f34, alpha, f50, f29, k);
                            this.vertex(pConsumer, d3, d1 + (double)f38, d4, f32, f33, f34, alpha, f49, f28, k);
                        }
                    }
                }
                cir.setReturnValue(flag7);
            }
        }
    }

    private void vertex(VertexConsumer pConsumer, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float alpha, float pU, float pV, int pPackedLight) {
        pConsumer.vertex(pX, pY, pZ).color(pRed, pGreen, pBlue, alpha).uv(pU, pV).uv2(pPackedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
    }
 */
}
