package dev.deepslate.fallacy.survive.diet.item

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.diet.NutritionContainer
import dev.deepslate.fallacy.survive.diet.NutritionType
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.StreamCodec

data class FoodNutrition(val container: NutritionContainer = NutritionContainer.EMPTY) :
    Iterable<NutritionContainer.Entry> by container {
    companion object {
        @JvmStatic
        val CODEC: Codec<FoodNutrition> = NutritionContainer.CODEC.xmap(
            ::FoodNutrition,
            FoodNutrition::container
        )

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, FoodNutrition> = NutritionContainer.STREAM_CODEC.map(
            ::FoodNutrition,
            FoodNutrition::container
        )
    }

    infix operator fun get(type: NutritionType) = container[type]

    infix operator fun get(holder: Holder<NutritionType>) = container[holder]

//    val components: Iterable<MutableComponent> get() = nutritionRecord.map(Nutrition::component)

//    fun toComponent() = toComponents().reduce { c, then -> c.append(then) }
}