package dev.deepslate.fallacy.survive.compat.hud

import dev.deepslate.fallacy.hud.client.LayerRender
import dev.deepslate.fallacy.hud.client.controller.FoodBarController
import dev.deepslate.fallacy.survive.TheMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent
import net.neoforged.fml.loading.FMLLoader

//Client side
@EventBusSubscriber(modid = TheMod.ID)
object Hook {
    @SubscribeEvent
    fun onLoadCompleted(event: FMLLoadCompleteEvent) {
        if (FMLLoader.getDist() != Dist.CLIENT) return
        if (!FMLLoader.getLoadingModList().mods.any { modInfo -> modInfo.modId == "fallacy_survive" }) return

        event.enqueueWork {
            val ui = LayerRender.INSTANCE.barUI

            ui.removeBy(FoodBarController::class.java)
            ui.add(ExFoodBarController())
            ui.add(ThirstBarController())
            TheMod.LOGGER.info("Added custom bar")
        }

        event.enqueueWork {
            val ui = LayerRender.INSTANCE.centerUI
            ui.setController(BodyHeatController())
            TheMod.LOGGER.info("Added custom body heat center ui")
        }
    }
}