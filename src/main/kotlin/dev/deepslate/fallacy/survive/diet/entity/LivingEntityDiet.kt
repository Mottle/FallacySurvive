package dev.deepslate.fallacy.survive.diet.entity

import dev.deepslate.fallacy.base.capability.Synchronous
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.survive.network.packet.FoodHistorySyncPacket
import dev.deepslate.fallacy.survive.network.packet.NutritionStateSyncPacket
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor

class LivingEntityDiet<E : LivingEntity>(override val entity: E) : Diet<E>, Synchronous {

    override var history: FoodHistory
        get() = entity.getData(ModAttachments.FOOD_HISTORY)
        set(value) {
            entity.setData(ModAttachments.FOOD_HISTORY, value)
//            markChanged()
            synchronize()
        }

    override var nutrition: LivingEntityNutritionState
        get() = entity.getData(ModAttachments.NUTRITION_STATE)
        set(value) {
            entity.setData(ModAttachments.NUTRITION_STATE, value)
//            markChanged()
            synchronize()
        }

    override fun synchronize() {
        if (entity !is ServerPlayer) return

        PacketDistributor.sendToPlayer(entity, NutritionStateSyncPacket(nutrition))
        PacketDistributor.sendToPlayer(entity, FoodHistorySyncPacket(history))
//        changed = false
    }

    override fun getFullEffectInstance(fullLevel: Int): MobEffectInstance {
        val ticks = seconds2Ticks(fullLevel * 20)
        return MobEffectInstance(ModEffects.FULL, ticks)
    }

    override fun handleNutrition(nutrition: FoodNutrition) {
        val nutritionState = entity.getData(ModAttachments.NUTRITION_STATE)
        val new = nutritionState.add(nutrition, entity)
        new.set(entity)
    }

    override fun getEatDurationMultiple(food: ItemStack): Float {
        val count = history.countFood(food)

        return when (count) {
            in 0..2 -> 1f
            in 3..4 -> 1.5f
            in 5..6 -> 3f
            in 7..8 -> 5f
            9 -> 8f
            else -> 12f
        }
    }

//    var changed = false
//        private set
//
//    fun markChanged() {
//        changed = true
//    }
}