package dev.deepslate.fallacy.survive.diet.entity

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.*
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import dev.deepslate.fallacy.survive.network.packet.NutritionStateSyncPacket
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor

/*
 * carbohydrate: 被动消耗
 * protein: 受伤消耗
 * fat: 寒冷地区消耗
 * fiber: 被动消耗
 * electrolyte: 运动消耗
 */
data class LivingEntityNutritionState(val container: NutrientContainer = NutrientContainer.EMPTY) :
    Iterable<NutrientContainer.Entry> by container {

    companion object {
        @JvmStatic
        val CODEC: Codec<LivingEntityNutritionState> = NutrientContainer.CODEC.xmap(
            ::LivingEntityNutritionState,
            LivingEntityNutritionState::container
        )

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, LivingEntityNutritionState> = NutrientContainer.STREAM_CODEC.map(
            ::LivingEntityNutritionState,
            LivingEntityNutritionState::container
        )

        @JvmStatic
        val DEFAULT = LivingEntityNutritionState(
            NutrientContainer.ofHolder(
                ModNutritionTypes.CARBOHYDRATE to 90f,
                ModNutritionTypes.PROTEIN to 90f,
                ModNutritionTypes.FAT to 90f,
                ModNutritionTypes.FIBER to 90f,
                ModNutritionTypes.ELECTROLYTE to 90f
            )
        )
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object Handler {
        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            val player = event.entity as? ServerPlayer ?: return
            val data = player.getData(ModAttachments.NUTRITION_STATE)
            val packet = NutritionStateSyncPacket(data)
            PacketDistributor.sendToPlayer(player, packet)
        }
    }

//    fun add(nutrition: Nutrition) = nutritionRecord.firstOrNull { it.match(nutrition) }
//        ?.let { matched ->
//            copy(nutritionRecord = nutritionRecord.map {
//                if (it === matched) it.add(nutrition.value) else it
//            })
//        }
//        ?: copy(nutritionRecord = nutritionRecord + nutrition)

    fun add(type: NutrientType, value: Float, entity: LivingEntity) =
        copy(container = container.update(type) { old -> old + value }).finetune(entity)

    fun add(holder: Holder<NutrientType>, value: Float, entity: LivingEntity) =
        copy(container = container.update(holder) { old -> old + value }).finetune(entity)

    fun add(foodNutrition: FoodNutrition, entity: LivingEntity) =
        copy(container = container + foodNutrition.container).finetune(entity)

    private fun finetune(entity: LivingEntity) = container.associate { (type, value) ->
        val maxAttributeValue = entity.getAttributeValue(type.attribute ?: type.alternativeAttribute!!).toFloat()
        type to value.coerceAtMost(maxAttributeValue)
    }.let(::NutrientContainer).let(::LivingEntityNutritionState)

    infix operator fun get(type: NutrientType) = container[type]

    infix operator fun get(holder: Holder<NutrientType>) = container[holder]

    infix operator fun contains(type: NutrientType) = container.contains(type)

    infix operator fun contains(holder: Holder<NutrientType>) = container.contains(holder)

    fun set(entity: LivingEntity) {
        entity.setData(ModAttachments.NUTRITION_STATE, this)
    }
}