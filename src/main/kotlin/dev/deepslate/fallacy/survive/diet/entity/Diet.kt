package dev.deepslate.fallacy.survive.diet.entity

import dev.deepslate.fallacy.survive.ModDataComponents
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

interface Diet<E : LivingEntity> {

    val entity: E

    var history: FoodHistory

    var nutrition: LivingEntityNutritionState

    //food必须保证可食用
    fun eat(food: ItemStack) {
        if (food.has(ModDataComponents.NUTRITION)) {
            val nutrition = food.get(ModDataComponents.NUTRITION)!!
            handleNutrition(nutrition)
        }

        val fullLevel = food.get(ModDataComponents.FULL_LEVEL) ?: 2
        val effect = getFullEffectInstance(fullLevel)
        entity.addEffect(effect)

        history = history.addFood(food)
    }

    fun getFullEffectInstance(fullLevel: Int): MobEffectInstance

    fun getEatDurationMultiple(food: ItemStack): Float

    fun handleNutrition(nutrition: FoodNutrition)
}