package dev.deepslate.fallacy.survive.diet

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class NutrientContainer(val nutritionMap: Map<NutrientType, Float>) : Iterable<NutrientContainer.Entry> {
    companion object {
        @JvmStatic
        val CODEC: Codec<NutrientContainer> = Codec.unboundedMap(NutrientType.CODEC, Codec.FLOAT)
            .xmap(::NutrientContainer, NutrientContainer::toHashMap)

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, NutrientContainer> =
            ByteBufCodecs.map(::HashMap, NutrientType.STREAM_CODEC, ByteBufCodecs.FLOAT, 16)
                .map(::NutrientContainer, NutrientContainer::toHashMap)

        fun empty() = NutrientContainer(emptyMap())

        @JvmStatic
        val EMPTY = empty()

        fun of(vararg pairs: Pair<NutrientType, Float>) = NutrientContainer(pairs.associate { it.first to it.second })

        fun ofHolder(vararg pairs: Pair<Holder<NutrientType>, Float>) =
            of(*pairs.map { it.first.value() to it.second }.toTypedArray())
    }

    private fun toHashMap() = HashMap(nutritionMap)

    infix operator fun get(type: NutrientType): Float = getOrNull(type) ?: 0f

    infix operator fun get(holder: Holder<NutrientType>): Float = get(holder.value())

    fun getOrNull(type: NutrientType): Float? = nutritionMap[type]

    fun getOrNull(holder: Holder<NutrientType>): Float? = getOrNull(holder.value())

    infix operator fun contains(type: NutrientType) = getOrNull(type) != null

    infix operator fun contains(holder: Holder<NutrientType>) = getOrNull(holder) != null

    infix operator fun plus(pair: Pair<NutrientType, Float>) =
        nutritionMap + (pair.first to (pair.second + (nutritionMap[pair.first] ?: 0F)))

    //Pure Fp implementation
//    infix operator fun plus(container: NutritionContainer) = (this.nutritionMap.keys + container.nutritionMap.keys)
//        .associateWith { type ->
//            (this.nutritionMap.getOrDefault(type, 0f) +
//                    container.nutritionMap.getOrDefault(type, 0f))
//        }.let(::NutritionContainer)

    infix operator fun plus(container: NutrientContainer): NutrientContainer {
        val newMap = HashMap(nutritionMap)
        container.nutritionMap.forEach { (type, value) ->
            newMap[type] = (newMap[type] ?: 0f) + value
        }
        return NutrientContainer(newMap)
    }

    fun update(type: NutrientType, value: Float) = NutrientContainer(nutritionMap + (type to value))

    fun update(holder: Holder<NutrientType>, value: Float) = update(holder.value(), value)

    fun update(type: NutrientType, valueGetter: (Float) -> Float) =
        NutrientContainer(nutritionMap + (type to (valueGetter(nutritionMap[type] ?: 0f))))

    fun update(holder: Holder<NutrientType>, valueGetter: (Float) -> Float) = update(holder.value(), valueGetter)

    fun remove(type: NutrientType) = NutrientContainer(nutritionMap - type)

    fun remove(holder: Holder<NutrientType>) = remove(holder.value())

//    val components: Iterable<MutableComponent> get() = nutrition.map(Nutrition::component)

    override fun iterator(): Iterator<Entry> = nutritionMap.entries.map { (k, v) -> Entry(k, v) }.iterator()

    data class Entry(val type: NutrientType, val value: Float)
}