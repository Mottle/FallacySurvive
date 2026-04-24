package dev.deepslate.fallacy.survive.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import dev.deepslate.fallacy.survive.hydration.block.BoilPotTank
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.inventory.InventoryMenu
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import org.joml.Matrix4f

class BoilPotBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<BoilPotEntity> {

    override fun render(
        entity: BoilPotEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val fluidStack = entity.content
        if (fluidStack.isEmpty) return

        val fluid = fluidStack.fluid
        val extensions = IClientFluidTypeExtensions.of(fluid)
        val tintColor = extensions.getTintColor(fluidStack)
        val stillTexture = extensions.getStillTexture(fluidStack)

        val sprite: TextureAtlasSprite = Minecraft.getInstance()
            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(stillTexture)

        poseStack.pushPose()

        val capacity = BoilPotTank.CAPACITY.toFloat()
        val amount = fluidStack.amount.toFloat()
        val fillRatio = amount / capacity

        val minY = 3f / 16f
        val maxY = 11f / 16f
        val height = minY + (maxY - minY) * fillRatio

        val minX = 3f / 16f
        val maxX = 13f / 16f
        val minZ = 3f / 16f
        val maxZ = 13f / 16f

        val u1 = sprite.u0
        val u2 = sprite.u1
        val v1 = sprite.v0
        val v2 = sprite.v1

        val a = (tintColor shr 24) and 0xFF
        val r = (tintColor shr 16) and 0xFF
        val g = (tintColor shr 8) and 0xFF
        val b = tintColor and 0xFF

        val vertexConsumer = buffer.getBuffer(RenderType.translucent())
        val matrix: Matrix4f = poseStack.last().pose()

        vertexConsumer.addVertex(matrix, minX, height, minZ)
            .setColor(r, g, b, a)
            .setUv(u1, v1)
            .setOverlay(packedOverlay)
            .setLight(packedLight)
            .setNormal(0f, 1f, 0f)

        vertexConsumer.addVertex(matrix, minX, height, maxZ)
            .setColor(r, g, b, a)
            .setUv(u1, v2)
            .setOverlay(packedOverlay)
            .setLight(packedLight)
            .setNormal(0f, 1f, 0f)

        vertexConsumer.addVertex(matrix, maxX, height, maxZ)
            .setColor(r, g, b, a)
            .setUv(u2, v2)
            .setOverlay(packedOverlay)
            .setLight(packedLight)
            .setNormal(0f, 1f, 0f)

        vertexConsumer.addVertex(matrix, maxX, height, minZ)
            .setColor(r, g, b, a)
            .setUv(u2, v1)
            .setOverlay(packedOverlay)
            .setLight(packedLight)
            .setNormal(0f, 1f, 0f)

        poseStack.popPose()
    }
}
