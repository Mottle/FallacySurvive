package dev.deepslate.fallacy.survive.diet

import dev.deepslate.fallacy.survive.TheMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModNutritionTypes {
    @JvmStatic
    private val registry = DeferredRegister.create(NutritionType.REGISTRY, TheMod.ID)

    @JvmStatic
    val CARBOHYDRATE: DeferredHolder<NutritionType, NutritionType> = registry.register("carbohydrate") { id ->
        NutritionType(id, displayColor = 0xff4500)
    }

    @JvmStatic
    val PROTEIN: DeferredHolder<NutritionType, NutritionType> = registry.register("protein") { id ->
        NutritionType(id, displayColor = 0xffa500)
    }

    @JvmStatic
    val FAT: DeferredHolder<NutritionType, NutritionType> = registry.register("fat") { id ->
        NutritionType(id, displayColor = 0x8b7e66)
    }

    @JvmStatic
    val FIBER: DeferredHolder<NutritionType, NutritionType> = registry.register("fiber") { id ->
        NutritionType(id, displayColor = 0x32cd32)
    }

    @JvmStatic
    val ELECTROLYTE: DeferredHolder<NutritionType, NutritionType> = registry.register("electrolyte") { id ->
        NutritionType(id, displayColor = 0x00ced1)
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModConstructed(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}