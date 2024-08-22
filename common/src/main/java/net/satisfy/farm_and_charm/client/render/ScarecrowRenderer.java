package net.satisfy.farm_and_charm.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.farm_and_charm.FarmAndCharm;
import net.satisfy.farm_and_charm.block.ScarecrowBlock;
import net.satisfy.farm_and_charm.block.entity.ScarecrowBlockEntity;
import net.satisfy.farm_and_charm.client.model.ScarecrowModel;
import org.joml.Quaternionf;

public class ScarecrowRenderer implements BlockEntityRenderer<ScarecrowBlockEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FarmAndCharm.MOD_ID, "textures/entity/scarecrow.png");
    private final ModelPart scarecrow;
    private final ModelPart post;
    private float swayAngle = 0.0F;
    private boolean swayForward = true;

    public ScarecrowRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(ScarecrowModel.LAYER_LOCATION);
        this.scarecrow = root.getChild("scarecrow");
        this.post = root.getChild("post");
    }

    private float updateSwayAngle(ScarecrowBlockEntity blockEntity) {
        float swayIncrement = 0.01f;
        if (swayForward) {
            swayAngle += swayIncrement;
            if (swayAngle >= 2.0f) {
                swayForward = false;
            }
        } else {
            swayAngle -= swayIncrement;
            if (swayAngle <= -2.0f) {
                swayForward = true;
            }
        }
        return swayAngle;
    }

    @Override
    public void render(ScarecrowBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        Direction direction = blockEntity.getBlockState().getValue(ScarecrowBlock.FACING);
        float rotationDegrees = -direction.toYRot() + 180;
        final var angle = updateSwayAngle(blockEntity);

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(rotationDegrees)));
        poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(angle)));
        poseStack.translate(-0.5, 0, -0.5);

        scarecrow.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY);
        post.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
