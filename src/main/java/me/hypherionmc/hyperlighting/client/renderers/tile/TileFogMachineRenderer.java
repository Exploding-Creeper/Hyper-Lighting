package me.hypherionmc.hyperlighting.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import me.hypherionmc.hyperlighting.common.blocks.FogMachineBlock;
import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import me.hypherionmc.hyperlighting.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileFogMachineRenderer implements BlockEntityRenderer<TileFogMachine> {

    public static final float TANK_THICKNESS = 0.1f;

    public TileFogMachineRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TileFogMachine pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity == null || pBlockEntity.isRemoved()) return;

        FluidStack stack = pBlockEntity.getFluidInTank(0);
        if (stack.isEmpty()) return;

        Fluid fluid = stack.getFluid();
        if (fluid == null) return;

        TextureAtlasSprite fluidTexture = RenderUtils.getFluidTexture(stack, true);

        VertexConsumer builder = pBufferSource.getBuffer(RenderType.translucent());
        float scale = (0.296f - TANK_THICKNESS / 2 - TANK_THICKNESS) * stack.getAmount() / (pBlockEntity.getTank().getCapacity());
        Vector4f color = RenderUtils.colorIntToRGBA(fluid.getAttributes().getColor());
        Direction direction = pBlockEntity.getBlockState().getValue(FogMachineBlock.FACING);

        pPoseStack.pushPose();
        pPoseStack.translate(0, 0, 0);

        if (direction == Direction.NORTH) {
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(0));
            pPoseStack.translate(0.343, 0.069, 0.77);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.SOUTH) {
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            pPoseStack.translate(-0.657, 0.069, -0.232);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.EAST) {
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
            pPoseStack.translate(0.34, 0.069, -0.232);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.WEST) {
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            pPoseStack.translate(-0.657, 0.069, 0.77);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }

        add(builder, pPoseStack, 0 + .8f, 0 + 1, .8f, fluidTexture.getU0(), fluidTexture.getV0(), color);
        add(builder, pPoseStack, 1 - .8f, 0 + 1, .8f, fluidTexture.getU1(), fluidTexture.getV0(), color);
        add(builder, pPoseStack, 1 - .8f, 1 - 1, .8f, fluidTexture.getU1(), fluidTexture.getV1(), color);
        add(builder, pPoseStack, 0 + .8f, 1 - 1, .8f, fluidTexture.getU0(), fluidTexture.getV1(), color);

        pPoseStack.popPose();
    }

    private void add(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v, Vector4f color) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(color.x(), color.y(), color.z(), color.w())
                .uv(u, v)
                .uv2(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
}
