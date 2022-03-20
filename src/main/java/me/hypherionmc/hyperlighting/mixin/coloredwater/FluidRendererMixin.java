package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import me.hypherionmc.hyperlighting.utils.OptiHacks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

/***
 * Optifine hack around BECAUSE THEY CAN'T STOP FUCKING UP CUSTOM FLUIDS!
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

    @Shadow
    @Final
    private Sprite[] lavaSprites;
    @Shadow
    @Final
    private Sprite[] waterSprites;
    @Shadow
    private Sprite waterOverlaySprite;

    @Shadow
    private static boolean isSameFluid(FluidState fluidState, FluidState fluidState2) {
        return false;
    }

    @Shadow
    private static boolean isSideCovered(BlockView blockView, BlockPos blockPos, Direction direction, float maxDeviation, BlockState blockState) {
        return false;
    }

    @Shadow
    public static boolean shouldRenderSide(BlockRenderView world, BlockPos blockPos, FluidState fluidState, BlockState blockState, Direction direction, FluidState fluidState2) {
        return false;
    }

    @Shadow
    private float method_40079(BlockRenderView blockRenderView, Fluid fluid, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return 0f;
    }

    @Shadow
    private int getLight(BlockRenderView world, BlockPos pos) {
        return 0;
    }

    @Shadow
    private void vertex(VertexConsumer vertexConsumer, double x, double y, double z, float red, float green, float blue, float u, float v, int light) {
    }

    @Shadow
    private float method_40077(BlockRenderView blockRenderView, Fluid fluid, float f, float g, float h, BlockPos blockPos) { return 0f; }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        // Only apply if fluid is Colored Water and Optifine is present
        if (fluidState.getFluid() instanceof ColoredWater coloredWater && OptiHacks.hasOptifine()) {
            boolean bl = fluidState.isIn(FluidTags.LAVA);
            Sprite[] sprites = bl ? this.lavaSprites : this.waterSprites;
            int i = bl ? 16777215 : BiomeColors.getWaterColor(world, pos);
            float f = (float)(i >> 16 & 255) / 255.0F;
            float g = (float)(i >> 8 & 255) / 255.0F;
            float h = (float)(i & 255) / 255.0F;
            BlockState blockState2 = world.getBlockState(pos.offset(Direction.DOWN));
            FluidState fluidState2 = blockState2.getFluidState();
            BlockState blockState3 = world.getBlockState(pos.offset(Direction.UP));
            FluidState fluidState3 = blockState3.getFluidState();
            BlockState blockState4 = world.getBlockState(pos.offset(Direction.NORTH));
            FluidState fluidState4 = blockState4.getFluidState();
            BlockState blockState5 = world.getBlockState(pos.offset(Direction.SOUTH));
            FluidState fluidState5 = blockState5.getFluidState();
            BlockState blockState6 = world.getBlockState(pos.offset(Direction.WEST));
            FluidState fluidState6 = blockState6.getFluidState();
            BlockState blockState7 = world.getBlockState(pos.offset(Direction.EAST));
            FluidState fluidState7 = blockState7.getFluidState();
            boolean bl2 = !isSameFluid(fluidState, fluidState3);
            boolean bl3 = shouldRenderSide(world, pos, fluidState, blockState, Direction.DOWN, fluidState2) && !isSideCovered(world, pos, Direction.DOWN, 0.8888889F, blockState2);
            boolean bl4 = shouldRenderSide(world, pos, fluidState, blockState, Direction.NORTH, fluidState4);
            boolean bl5 = shouldRenderSide(world, pos, fluidState, blockState, Direction.SOUTH, fluidState5);
            boolean bl6 = shouldRenderSide(world, pos, fluidState, blockState, Direction.WEST, fluidState6);
            boolean bl7 = shouldRenderSide(world, pos, fluidState, blockState, Direction.EAST, fluidState7);
            if (!bl2 && !bl3 && !bl7 && !bl6 && !bl4 && !bl5) {
                cir.setReturnValue(false);
            } else {
                boolean bl8 = false;
                float j = world.getBrightness(Direction.DOWN, true);
                float k = world.getBrightness(Direction.UP, true);
                float l = world.getBrightness(Direction.NORTH, true);
                float m = world.getBrightness(Direction.WEST, true);
                Fluid fluid = fluidState.getFluid();
                float n = this.method_40079(world, fluid, pos, blockState, fluidState);
                float o;
                float p;
                float q;
                float r;

                if (n >= 1.0F) {
                    o = 1.0F;
                    p = 1.0F;
                    q = 1.0F;
                    r = 1.0F;
                } else {
                    float s = this.method_40079(world, fluid, pos.north(), blockState4, fluidState4);
                    float t = this.method_40079(world, fluid, pos.south(), blockState5, fluidState5);
                    float u = this.method_40079(world, fluid, pos.east(), blockState7, fluidState7);
                    float v = this.method_40079(world, fluid, pos.west(), blockState6, fluidState6);
                    o = this.method_40077(world, fluid, n, s, u, pos.offset(Direction.NORTH).offset(Direction.EAST));
                    p = this.method_40077(world, fluid, n, s, v, pos.offset(Direction.NORTH).offset(Direction.WEST));
                    q = this.method_40077(world, fluid, n, t, u, pos.offset(Direction.SOUTH).offset(Direction.EAST));
                    r = this.method_40077(world, fluid, n, t, v, pos.offset(Direction.SOUTH).offset(Direction.WEST));
                }

                double s = (double)(pos.getX() & 15);
                double u = (double)(pos.getY() & 15);
                double d = (double)(pos.getZ() & 15);
                float w = 0.001F;
                float x = bl3 ? 0.001F : 0.0F;
                float y;
                float aa;
                float ac;
                float ae;
                float z;
                float ab;
                float ad;
                float af;
                float am;
                float an;

                if (OptiHacks.isRenderRegions()) {
                    int pj = pos.getX() >> 4 << 4;
                    int kk = pos.getY() >> 4 << 4;
                    int ll = pos.getZ() >> 4 << 4;
                    int ii1 = 8;
                    int j1 = pj >> ii1 << ii1;
                    int k1 = ll >> ii1 << ii1;
                    int l1 = pj - j1;
                    int i2 = ll - k1;
                    s += l1;
                    u += kk;
                    d += i2;
                }

                if (bl2 && !isSideCovered(world, pos, Direction.UP, Math.min(Math.min(p, r), Math.min(q, o)), blockState3)) {
                    bl8 = true;
                    p -= 0.001F;
                    r -= 0.001F;
                    q -= 0.001F;
                    o -= 0.001F;
                    Vec3d vec3d = fluidState.getVelocity(world, pos);
                    Sprite sprite;
                    float ag;
                    float ah;
                    float ai;
                    float aj;
                    if (vec3d.x == 0.0D && vec3d.z == 0.0D) {
                        sprite = sprites[0];
                        y = sprite.getFrameU(0.0D);
                        z = sprite.getFrameV(0.0D);
                        aa = y;
                        ab = sprite.getFrameV(16.0D);
                        ac = sprite.getFrameU(16.0D);
                        ad = ab;
                        ae = ac;
                        af = z;
                    } else {
                        sprite = sprites[1];
                        ag = (float)MathHelper.atan2(vec3d.z, vec3d.x) - 1.5707964F;
                        ah = MathHelper.sin(ag) * 0.25F;
                        ai = MathHelper.cos(ag) * 0.25F;
                        aj = 8.0F;
                        y = sprite.getFrameU((double)(8.0F + (-ai - ah) * 16.0F));
                        z = sprite.getFrameV((double)(8.0F + (-ai + ah) * 16.0F));
                        aa = sprite.getFrameU((double)(8.0F + (-ai + ah) * 16.0F));
                        ab = sprite.getFrameV((double)(8.0F + (ai + ah) * 16.0F));
                        ac = sprite.getFrameU((double)(8.0F + (ai + ah) * 16.0F));
                        ad = sprite.getFrameV((double)(8.0F + (ai - ah) * 16.0F));
                        ae = sprite.getFrameU((double)(8.0F + (ai - ah) * 16.0F));
                        af = sprite.getFrameV((double)(8.0F + (-ai - ah) * 16.0F));
                    }

                    float sprt = (y + aa + ac + ae) / 4.0F;
                    ag = (z + ab + ad + af) / 4.0F;
                    ah = (float)sprites[0].getWidth() / (sprites[0].getMaxU() - sprites[0].getMinU());
                    ai = (float)sprites[0].getHeight() / (sprites[0].getMaxV() - sprites[0].getMinV());
                    aj = 4.0F / Math.max(ai, ah);
                    y = MathHelper.lerp(aj, y, sprt);
                    aa = MathHelper.lerp(aj, aa, sprt);
                    ac = MathHelper.lerp(aj, ac, sprt);
                    ae = MathHelper.lerp(aj, ae, sprt);
                    z = MathHelper.lerp(aj, z, ag);
                    ab = MathHelper.lerp(aj, ab, ag);
                    ad = MathHelper.lerp(aj, ad, ag);
                    af = MathHelper.lerp(aj, af, ag);
                    int ak = this.getLight(world, pos);
                    float al = k * f;
                    am = k * g;
                    an = k * h;
                    this.vertex(vertexConsumer, s + 0.0D, u + (double)p, d + 0.0D, al, am, an, y, z, ak);
                    this.vertex(vertexConsumer, s + 0.0D, u + (double)r, d + 1.0D, al, am, an, aa, ab, ak);
                    this.vertex(vertexConsumer, s + 1.0D, u + (double)q, d + 1.0D, al, am, an, ac, ad, ak);
                    this.vertex(vertexConsumer, s + 1.0D, u + (double)o, d + 0.0D, al, am, an, ae, af, ak);
                    if (fluidState.method_15756(world, pos.up())) {
                        this.vertex(vertexConsumer, s + 0.0D, u + (double)p, d + 0.0D, al, am, an, y, z, ak);
                        this.vertex(vertexConsumer, s + 1.0D, u + (double)o, d + 0.0D, al, am, an, ae, af, ak);
                        this.vertex(vertexConsumer, s + 1.0D, u + (double)q, d + 1.0D, al, am, an, ac, ad, ak);
                        this.vertex(vertexConsumer, s + 0.0D, u + (double)r, d + 1.0D, al, am, an, aa, ab, ak);
                    }
                }

                if (bl3) {
                    y = sprites[0].getMinU();
                    aa = sprites[0].getMaxU();
                    ac = sprites[0].getMinV();
                    ae = sprites[0].getMaxV();
                    int zz = this.getLight(world, pos.down());
                    ab = j * f;
                    ad = j * g;
                    af = j * h;
                    this.vertex(vertexConsumer, s, u + (double)x, d + 1.0D, ab, ad, af, y, ae, zz);
                    this.vertex(vertexConsumer, s, u + (double)x, d, ab, ad, af, y, ac, zz);
                    this.vertex(vertexConsumer, s + 1.0D, u + (double)x, d, ab, ad, af, aa, ac, zz);
                    this.vertex(vertexConsumer, s + 1.0D, u + (double)x, d + 1.0D, ab, ad, af, aa, ae, zz);
                    bl8 = true;
                }

                int yy = this.getLight(world, pos);
                Iterator var77 = Direction.Type.HORIZONTAL.iterator();

                while(true) {
                    Direction acc;
                    double abc;
                    double afc;
                    double sprite;
                    double ah;
                    boolean aj;
                    do {
                        do {
                            if (!var77.hasNext()) {
                                cir.setReturnValue(bl8);
                            }

                            acc = (Direction)var77.next();
                            switch(acc) {
                                case NORTH:
                                    ae = p;
                                    z = o;
                                    abc = s;
                                    sprite = s + 1.0D;
                                    afc = d + 0.0010000000474974513D;
                                    ah = d + 0.0010000000474974513D;
                                    aj = bl4;
                                    break;
                                case SOUTH:
                                    ae = q;
                                    z = r;
                                    abc = s + 1.0D;
                                    sprite = s;
                                    afc = d + 1.0D - 0.0010000000474974513D;
                                    ah = d + 1.0D - 0.0010000000474974513D;
                                    aj = bl5;
                                    break;
                                case WEST:
                                    ae = r;
                                    z = p;
                                    abc = s + 0.0010000000474974513D;
                                    sprite = s + 0.0010000000474974513D;
                                    afc = d + 1.0D;
                                    ah = d;
                                    aj = bl6;
                                    break;
                                default:
                                    ae = o;
                                    z = q;
                                    abc = s + 1.0D - 0.0010000000474974513D;
                                    sprite = s + 1.0D - 0.0010000000474974513D;
                                    afc = d;
                                    ah = d + 1.0D;
                                    aj = bl7;
                            }
                        } while(!aj);
                    } while(isSideCovered(world, pos, acc, Math.max(ae, z), world.getBlockState(pos.offset(acc))));

                    bl8 = true;
                    BlockPos ak = pos.offset(acc);
                    Sprite al = sprites[1];
                    if (!bl) {
                        Block amc = world.getBlockState(ak).getBlock();
                        if (amc instanceof TransparentBlock || amc instanceof LeavesBlock) {
                            al = this.waterOverlaySprite;
                        }
                    }

                    am = al.getFrameU(0.0D);
                    an = al.getFrameU(8.0D);
                    float ao = al.getFrameV((double)((1.0F - ae) * 16.0F * 0.5F));
                    float ap = al.getFrameV((double)((1.0F - z) * 16.0F * 0.5F));
                    float aq = al.getFrameV(8.0D);
                    float ar = acc.getAxis() == Direction.Axis.Z ? l : m;
                    float as = k * ar * f;
                    float at = k * ar * g;
                    float au = k * ar * h;
                    this.vertex(vertexConsumer, abc, u + (double)ae, afc, as, at, au, am, ao, yy);
                    this.vertex(vertexConsumer, sprite, u + (double)z, ah, as, at, au, an, ap, yy);
                    this.vertex(vertexConsumer, sprite, u + (double)x, ah, as, at, au, an, aq, yy);
                    this.vertex(vertexConsumer, abc, u + (double)x, afc, as, at, au, am, aq, yy);
                    if (al != this.waterOverlaySprite) {
                        this.vertex(vertexConsumer, abc, u + (double)x, afc, as, at, au, am, aq, yy);
                        this.vertex(vertexConsumer, sprite, u + (double)x, ah, as, at, au, an, aq, yy);
                        this.vertex(vertexConsumer, sprite, u + (double)z, ah, as, at, au, an, ap, yy);
                        this.vertex(vertexConsumer, abc, u + (double)ae, afc, as, at, au, am, ao, yy);
                    }
                }
            }
        }
    }
}
