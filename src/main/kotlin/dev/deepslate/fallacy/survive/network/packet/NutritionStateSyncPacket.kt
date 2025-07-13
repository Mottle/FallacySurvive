package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.entity.LivingEntityNutritionState
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class NutritionStateSyncPacket(val state: LivingEntityNutritionState) : CustomPacketPayload {

    companion object {
        @JvmStatic
        val TYPE = CustomPacketPayload.Type<NutritionStateSyncPacket>(TheMod.withID("nutrition_state_sync_packet"))

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionStateSyncPacket> =
            StreamCodec.composite(
                LivingEntityNutritionState.STREAM_CODEC,
                NutritionStateSyncPacket::state,
                ::NutritionStateSyncPacket
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}