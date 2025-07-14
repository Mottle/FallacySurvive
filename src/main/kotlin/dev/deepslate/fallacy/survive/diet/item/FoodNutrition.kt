package dev.deepslate.fallacy.survive.diet.item

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.diet.NutrientContainer
import dev.deepslate.fallacy.survive.diet.NutrientType
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.StreamCodec

data class FoodNutrition(val container: NutrientContainer = NutrientContainer.EMPTY) :
    Iterable<NutrientContainer.Entry> by container {
    companion object {
        @JvmStatic
        val CODEC: Codec<FoodNutrition> = NutrientContainer.CODEC.xmap(
            ::FoodNutrition,
            FoodNutrition::container
        )

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, FoodNutrition> = NutrientContainer.STREAM_CODEC.map(
            ::FoodNutrition,
            FoodNutrition::container
        )
    }

    infix operator fun get(type: NutrientType) = container[type]

    infix operator fun get(holder: Holder<NutrientType>) = container[holder]

//    val components: Iterable<MutableComponent> get() = nutritionRecord.map(Nutrition::component)

//    fun toComponent() = toComponents().reduce { c, then -> c.append(then) }
}