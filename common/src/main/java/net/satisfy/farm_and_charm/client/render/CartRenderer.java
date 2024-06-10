package net.satisfy.farm_and_charm.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.satisfy.farm_and_charm._jason.entity.JasonSupplyCartEntity;
import net.satisfy.farm_and_charm.client.model.CartModel;
import net.satisfy.farm_and_charm.entity.CartEntity;
import net.satisfy.farm_and_charm.util.FarmAndCharmIdentifier;
import org.jetbrains.annotations.NotNull;

public class CartRenderer extends EntityRenderer<CartEntity> {
    public static final ResourceLocation CART_TEXTURE = new FarmAndCharmIdentifier("textures/entity/supply_cart.png");
    private final CartModel<CartEntity> model;

    public CartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CartModel<>(context.bakeLayer(CartModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(CartEntity entity) {
        return CART_TEXTURE;
    }

    @Override
    public void render(CartEntity cart, float yaw, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        super.render(cart, yaw, g, poseStack, multiBufferSource, light);
        poseStack.pushPose();

        poseStack.translate(0.0D, 1.4D, 0.0D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

        this.model.setupAnim(cart, 0.0F, 0.0F, cart.level().getGameTime(), yaw, cart.getXRot());
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(CartRenderer.CART_TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}