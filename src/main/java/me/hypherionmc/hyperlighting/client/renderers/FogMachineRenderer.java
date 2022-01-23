package me.hypherionmc.hyperlighting.client.renderers;

import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.common.blocks.FogMachineBlock;
import me.hypherionmc.hyperlighting.utils.RenderUtils;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

public class FogMachineRenderer implements BlockEntityRenderer<FogMachineBlockEntity> {

    public static final float TANK_THICKNESS = 0.1f;

    public FogMachineRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(FogMachineBlockEntity pBlockEntity, float tickDelta, MatrixStack pPoseStack, VertexConsumerProvider pBufferSource, int light, int overlay) {
        if (pBlockEntity == null || pBlockEntity.isRemoved()) return;

        FluidVariant stack = pBlockEntity.getTank().getResource();
        if (stack.isBlank()) return;

        Fluid fluid = stack.getFluid();
        if (fluid == null) return;

        Sprite fluidTexture = RenderUtils.getFluidTexture(stack);

        VertexConsumer builder = pBufferSource.getBuffer(RenderLayer.getTranslucent());
        float scale = (0.296f - TANK_THICKNESS / 2 - TANK_THICKNESS) * pBlockEntity.getTank().getAmount() / (pBlockEntity.getTank().getCapacity());
        Vector4f color = RenderUtils.colorIntToRGBA(FluidVariantRendering.getColor(stack));
        Direction direction = pBlockEntity.getCachedState().get(FogMachineBlock.FACING);

        pPoseStack.push();
        pPoseStack.translate(0, 0, 0);

        if (direction == Direction.NORTH) {
            pPoseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            pPoseStack.translate(0.343, 0.069, 0.77);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.SOUTH) {
            pPoseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            pPoseStack.translate(-0.657, 0.069, -0.232);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.EAST) {
            pPoseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
            pPoseStack.translate(0.34, 0.069, -0.232);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }
        if (direction == Direction.WEST) {
            pPoseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            pPoseStack.translate(-0.657, 0.069, 0.77);
            pPoseStack.scale(0.05f, scale, 0.15f);
        }

        add(builder, pPoseStack, 0 + .8f, 0 + 1, .8f, fluidTexture.getMinU(), fluidTexture.getMinV(), color);
        add(builder, pPoseStack, 1 - .8f, 0 + 1, .8f, fluidTexture.getMaxU(), fluidTexture.getMinV(), color);
        add(builder, pPoseStack, 1 - .8f, 1 - 1, .8f, fluidTexture.getMaxU(), fluidTexture.getMaxV(), color);
        add(builder, pPoseStack, 0 + .8f, 1 - 1, .8f, fluidTexture.getMinU(), fluidTexture.getMaxV(), color);

        pPoseStack.pop();
    }

    private void add(VertexConsumer renderer, MatrixStack stack, float x, float y, float z, float u, float v, Vector4f color) {
        renderer.vertex(stack.peek().getPositionMatrix(), x, y, z)
                .color(color.getX(), color.getY(), color.getZ(), color.getW())
                .texture(u, v)
                .light(0, 240)
                .normal(1, 0, 0)
                .next();
    }
}
