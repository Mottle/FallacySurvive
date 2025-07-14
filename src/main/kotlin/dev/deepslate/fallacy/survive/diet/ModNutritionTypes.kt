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
    private val registry = DeferredRegister.create(NutrientType.REGISTRY, TheMod.ID)

    @JvmStatic
    val CARBOHYDRATE: DeferredHolder<NutrientType, NutrientType> = registry.register("carbohydrate") { id ->
        NutrientType(id, displayColor = 0xff4500)
    }

    @JvmStatic
    val PROTEIN: DeferredHolder<NutrientType, NutrientType> = registry.register("protein") { id ->
        NutrientType(id, displayColor = 0xffa500)
    }

    @JvmStatic
    val FAT: DeferredHolder<NutrientType, NutrientType> = registry.register("fat") { id ->
        NutrientType(id, displayColor = 0x8b7e66)
    }

    @JvmStatic
    val FIBER: DeferredHolder<NutrientType, NutrientType> = registry.register("fiber") { id ->
        NutrientType(id, displayColor = 0x32cd32)
    }

    @JvmStatic
    val ELECTROLYTE: DeferredHolder<NutrientType, NutrientType> = registry.register("electrolyte") { id ->
        NutrientType(id, displayColor = 0x00ced1)
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