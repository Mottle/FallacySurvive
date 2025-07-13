package dev.deepslate.fallacy.survive.diet.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.FoodHistorySyncPacket
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

data class FoodHistory(val foods: List<ResourceLocation> = listOf()) {

    companion object {
        const val MAX_SIZE = 10

        val CODEC: Codec<FoodHistory> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.sizeLimitedListOf(MAX_SIZE).fieldOf("foods").forGetter(FoodHistory::foods)
            ).apply(instance, ::FoodHistory)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, FoodHistory> = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), FoodHistory::foods,
            ::FoodHistory
        )
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object Handler {
        fun handleSync(data: FoodHistorySyncPacket, context: IPayloadContext) {
            context.player().setData(ModAttachments.FOOD_HISTORY, data.history)
            TheMod.LOGGER.info("Syncing food history.")
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            PacketDistributor.sendToPlayer(
                event.entity as ServerPlayer,
                FoodHistorySyncPacket(event.entity.getData(ModAttachments.FOOD_HISTORY))
            )
        }

        @SubscribeEvent
        fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
            PacketDistributor.sendToPlayer(
                event.entity as ServerPlayer,
                FoodHistorySyncPacket(event.entity.getData(ModAttachments.FOOD_HISTORY))
            )
        }
    }

    val size: Int
        get() = foods.size

    fun add(id: ResourceLocation): FoodHistory {
        if (size >= MAX_SIZE) return FoodHistory(foods.dropLast(1) + id)
        return FoodHistory(foods + id)
    }

    fun addFood(food: ItemStack): FoodHistory {
        if (!BuiltInRegistries.ITEM.containsValue(food.item)) return this

        val id = BuiltInRegistries.ITEM.getKey(food.item)
        return add(id)
    }

    fun count(id: ResourceLocation): Int = foods.count { it == id }

    fun countFood(food: ItemStack): Int = count(BuiltInRegistries.ITEM.getKey(food.item))
}