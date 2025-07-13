package dev.deepslate.fallacy.survive.client.tooltip

import com.mojang.datafixers.util.Either
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.ModDataComponents
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.entity.FoodHistory
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent

@EventBusSubscriber(modid = TheMod.ID)
object Handler {

    @SubscribeEvent
    fun onFoodTooltip(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack
        if (!stack.has(DataComponents.FOOD)) return

        val player = Minecraft.getInstance().player ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!
        val eatTimes = diet.history.countFood(stack)
        val foodNutrition = stack.get(ModDataComponents.NUTRITION)
        val fullLevel = stack.get(ModDataComponents.FULL_LEVEL) ?: 2

        event.tooltipElements.add(
            Either.left(
                Component.translatable("item.tooltips.full_level$fullLevel")
                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
            )
        )

        if (foodNutrition != null) {
            for ((type, value) in foodNutrition) {
                addNutrition(event, type.component, value)
            }
//            addNutrition(event, "item.fallacy.diet_data.carbohydrate", nutrition.carbohydrate)
//            addNutrition(event, "item.fallacy.diet_data.protein", nutrition.protein)
//            addNutrition(event, "item.fallacy.diet_data.fat", nutrition.fat)
//            addNutrition(event, "item.fallacy.diet_data.fiber", nutrition.fiber)
//            addNutrition(event, "item.fallacy.diet_data.electrolyte", nutrition.electrolyte)
        }

        if (eatTimes != 0) {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.food_eat_times", FoodHistory.MAX_SIZE, eatTimes)
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE)
                )
            )
        } else {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.food_never_eat")
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE)
                )
            )
        }
    }

    private fun addNutrition(event: RenderTooltipEvent.GatherComponents, translateKey: String, value: Float) {
        if (value <= 0) return
        val formated = "%.1f".format(value)
        event.tooltipElements.add(
            Either.left(
                Component.translatable(translateKey).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE)
                    .append(Component.literal(": $formated%").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE))
            )
        )
    }

    private fun addNutrition(event: RenderTooltipEvent.GatherComponents, component: MutableComponent, value: Float) {
        if (value <= 0) return
        val formated = "%.1f".format(value)
        event.tooltipElements.add(
            Either.left(
                component.withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE)
                    .append(Component.literal(": $formated%").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE))
            )
        )
    }
}