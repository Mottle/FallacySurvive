package dev.deepslate.fallacy.survive.diet

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class NutritionContainer(val nutritionMap: Map<NutritionType, Float>) : Iterable<NutritionContainer.Entry> {
    companion object {
        @JvmStatic
        val CODEC: Codec<NutritionContainer> = Codec.unboundedMap(NutritionType.CODEC, Codec.FLOAT)
            .xmap(::NutritionContainer, NutritionContainer::toHashMap)

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionContainer> =
            ByteBufCodecs.map(::HashMap, NutritionType.STREAM_CODEC, ByteBufCodecs.FLOAT, 16)
                .map(::NutritionContainer, NutritionContainer::toHashMap)

        fun empty() = NutritionContainer(emptyMap())

        @JvmStatic
        val EMPTY = empty()

        fun of(vararg pairs: Pair<NutritionType, Float>) = NutritionContainer(pairs.associate { it.first to it.second })

        fun ofHolder(vararg pairs: Pair<Holder<NutritionType>, Float>) =
            of(*pairs.map { it.first.value() to it.second }.toTypedArray())
    }

    private fun toHashMap() = HashMap(nutritionMap)

    infix operator fun get(type: NutritionType): Float = getOrNull(type) ?: 0f

    infix operator fun get(holder: Holder<NutritionType>): Float = get(holder.value())

    fun getOrNull(type: NutritionType): Float? = nutritionMap[type]

    fun getOrNull(holder: Holder<NutritionType>): Float? = getOrNull(holder.value())

    infix operator fun contains(type: NutritionType) = getOrNull(type) != null

    infix operator fun contains(holder: Holder<NutritionType>) = getOrNull(holder) != null

    infix operator fun plus(pair: Pair<NutritionType, Float>) =
        nutritionMap + (pair.first to (pair.second + (nutritionMap[pair.first] ?: 0F)))

    //Pure Fp implementation
//    infix operator fun plus(container: NutritionContainer) = (this.nutritionMap.keys + container.nutritionMap.keys)
//        .associateWith { type ->
//            (this.nutritionMap.getOrDefault(type, 0f) +
//                    container.nutritionMap.getOrDefault(type, 0f))
//        }.let(::NutritionContainer)

    infix operator fun plus(container: NutritionContainer): NutritionContainer {
        val newMap = HashMap(nutritionMap)
        container.nutritionMap.forEach { (type, value) ->
            newMap[type] = (newMap[type] ?: 0f) + value
        }
        return NutritionContainer(newMap)
    }

    private fun format(pair: Pair<NutritionType, Float>) =
        pair.first to pair.second.coerceIn(pair.first.minValue, pair.first.minValue)

    fun update(type: NutritionType, value: Float) = NutritionContainer(nutritionMap + (type to value).let(::format))

    fun update(holder: Holder<NutritionType>, value: Float) = update(holder.value(), value)

    fun update(type: NutritionType, valueGetter: (Float) -> Float) =
        NutritionContainer(nutritionMap + (type to (valueGetter(nutritionMap[type] ?: 0f))).let(::format))

    fun update(holder: Holder<NutritionType>, valueGetter: (Float) -> Float) = update(holder.value(), valueGetter)

    fun remove(type: NutritionType) = NutritionContainer(nutritionMap - type)

    fun remove(holder: Holder<NutritionType>) = remove(holder.value())

//    val components: Iterable<MutableComponent> get() = nutrition.map(Nutrition::component)

    override fun iterator(): Iterator<Entry> = nutritionMap.entries.map { (k, v) -> Entry(k, v) }.iterator()

    data class Entry(val type: NutritionType, val value: Float)
}