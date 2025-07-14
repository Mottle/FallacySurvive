package dev.deepslate.fallacy.survive.diet.item

import dev.deepslate.fallacy.survive.ModDataComponents
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.inject.itemfoodproperties.ItemFoodPropertiesExtension
import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

data class ExtendedFoodProperties(val fullLevel: Int, val nutrition: FoodNutrition, val eatDurationTicks: Int) {
    class Builder {

        //聊胜于无-零嘴-简餐-大餐-饕餮盛宴
        private var fullLevel: Int = 2

        private var nutrition: FoodNutrition = FoodNutrition()

        private var eatenDurationTicks: Int = -1

        //8 16 32 48 64
        private fun getEatenTicks(fullLevel: Int): Int = if (fullLevel == 0) 8 else fullLevel * 16

        fun withFullLevel(fullLevel: Int): Builder {
            this.fullLevel = fullLevel
            return this
        }

        fun withNutrition(defaultDiet: FoodNutrition): Builder {
            this.nutrition = defaultDiet
            return this
        }

        fun withEatenDurationTicks(eatenDurationTicks: Int): Builder {
            this.eatenDurationTicks = eatenDurationTicks
            return this
        }

        fun build(): ExtendedFoodProperties {
            val eatenDuration = if (eatenDurationTicks != -1) eatenDurationTicks else getEatenTicks(fullLevel)

            return ExtendedFoodProperties(fullLevel, nutrition, eatenDuration)
        }
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object Handler {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onModifyDefaultComponent(event: ModifyDefaultComponentsEvent) {
            event.allItems.filter { it.components().has(DataComponents.FOOD) }
                .filter { (it as ItemFoodPropertiesExtension).`fallacy$getExtendedFoodProperties`() != null }
                .forEach { item ->
                    val foodData = item.components().get(DataComponents.FOOD)!!
                    val properties = (item as ItemFoodPropertiesExtension).`fallacy$getExtendedFoodProperties`()!!

                    val fixedFoodData = FoodProperties(
                        foodData.nutrition,
                        foodData.saturation,
                        foodData.canAlwaysEat,
                        properties.eatDurationTicks.toFloat() / 20f,
                        foodData.usingConvertsTo,
                        foodData.effects
                    )

                    event.modify(item) { builder ->
                        builder.set(DataComponents.FOOD, fixedFoodData)
                        builder.set(ModDataComponents.NUTRITION.get(), properties.nutrition)
                        builder.set(ModDataComponents.FULL_LEVEL.get(), properties.fullLevel)
                    }
                }
        }
    }
}