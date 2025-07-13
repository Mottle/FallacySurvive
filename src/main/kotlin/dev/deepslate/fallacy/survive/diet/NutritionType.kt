package dev.deepslate.fallacy.survive.diet

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

data class NutritionType(
    val id: ResourceLocation,
    val minValue: Float = -100f,
    val maxValue: Float = 100f,
    val displayColor: Int = 0xff4500
) {
    companion object {
        @JvmStatic
        val KEY: ResourceKey<Registry<NutritionType>> = ResourceKey.createRegistryKey(TheMod.withID("nutrition"))

        @JvmStatic
        val REGISTRY: Registry<NutritionType> = RegistryBuilder(KEY).sync(true).maxId(256).create()

        @JvmStatic
        val CODEC: Codec<NutritionType> = ResourceLocation.CODEC.xmap(::NutritionType, NutritionType::id)

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionType> =
            ResourceLocation.STREAM_CODEC.map(::NutritionType, NutritionType::id)

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
        if (other !is NutritionType) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = minValue.hashCode()
        result = 31 * result + maxValue.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + component.hashCode()
        return result
    }
}