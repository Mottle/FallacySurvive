package dev.deepslate.fallacy.survive

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModDataComponents {
    private val registry = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TheMod.ID)

    //食物营养度
    val NUTRITION: DeferredHolder<DataComponentType<*>, DataComponentType<FoodNutrition>> =
        registry.registerComponentType("nutrition") { builder ->
            builder.persistent(FoodNutrition.Companion.CODEC).networkSynchronized(FoodNutrition.Companion.STREAM_CODEC)
        }

    //食物饱腹等级 默认2
    val FULL_LEVEL: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        registry.registerComponentType("full_level") { builder ->
            builder.persistent(Codec.intRange(0, 4)).networkSynchronized(ByteBufCodecs.INT)
        }

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModLoadCompleted(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}