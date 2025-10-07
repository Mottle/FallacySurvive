package dev.deepslate.fallacy.survive.diet

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder
import kotlin.jvm.optionals.getOrNull

data class NutrientType(val id: ResourceLocation) {
    companion object {

        private const val HASH_BASE = 10007 * 31

        @JvmStatic
        val KEY: ResourceKey<Registry<NutrientType>> = ResourceKey.createRegistryKey(TheMod.withID("nutrition"))

        @JvmStatic
        val REGISTRY: Registry<NutrientType> = RegistryBuilder(KEY).sync(true).maxId(256).create()

        @JvmStatic
        val CODEC: Codec<NutrientType> = ResourceLocation.CODEC.xmap(::NutrientType, NutrientType::id)

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, NutrientType> =
            ResourceLocation.STREAM_CODEC.map(::NutrientType, NutrientType::id)

//        @JvmStatic
//        val CODEC: Codec<NutritionType> = RecordCodecBuilder.create { instance ->
//            instance.group(
//                ResourceLocation.CODEC.fieldOf("id").forGetter(NutritionType::id),
//                Codec.FLOAT.fieldOf("min").forGetter(NutritionType::minValue),
//                Codec.FLOAT.fieldOf("max").forGetter(NutritionType::maxValue),
//                Codec.INT.fieldOf("color").forGetter(NutritionType::displayColor)
//            ).apply(instance, ::NutritionType)
//        }
//
//        @JvmStatic
//        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionType> = StreamCodec.composite(
//            ResourceLocation.STREAM_CODEC, NutritionType::id,
//            ByteBufCodecs.FLOAT, NutritionType::minValue,
//            ByteBufCodecs.FLOAT, NutritionType::maxValue,
//            ByteBufCodecs.INT, NutritionType::displayColor,
//            ::NutritionType
//        )
    }

    init {
        TheMod.REGISTRATE.addLang("nutrition", id, id.path.replaceFirstChar(Char::uppercase))
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onRegister(event: NewRegistryEvent) {
            event.register(REGISTRY)
        }
    }

    val component: MutableComponent get() = Component.translatable("nutrition.${id.namespace}.${id.path}")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NutrientType) return false
        return id == other.id
    }

    override fun hashCode(): Int = HASH_BASE + id.hashCode()
}

val NutrientType.attributeID: ResourceLocation
    get() = ResourceLocation.fromNamespaceAndPath(
        id.namespace,
        "generic.max_${id.path}"
    )

val NutrientType.attribute get() = BuiltInRegistries.ATTRIBUTE.getHolder(attributeID).getOrNull()

val NutrientType.alternativeAttributeID: ResourceLocation
    get() = ResourceLocation.fromNamespaceAndPath(
        id.namespace,
        "player.max_${id.path}"
    )

val NutrientType.alternativeAttribute get() = BuiltInRegistries.ATTRIBUTE.getHolder(alternativeAttributeID).getOrNull()