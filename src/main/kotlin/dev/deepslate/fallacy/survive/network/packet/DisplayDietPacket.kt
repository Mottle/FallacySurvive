package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.entity.LivingEntityNutritionState
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class DisplayDietPacket(val nutrition: LivingEntityNutritionState) : CustomPacketPayload {

    companion object {
        @JvmStatic
        val TYPE = CustomPacketPayload.Type<DisplayDietPacket>(TheMod.withID("display_diet_packet"))

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, DisplayDietPacket> = LivingEntityNutritionState.STREAM_CODEC.map(
            ::DisplayDietPacket,
            DisplayDietPacket::nutrition
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}