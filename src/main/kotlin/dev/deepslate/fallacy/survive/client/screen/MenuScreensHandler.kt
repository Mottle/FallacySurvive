package dev.deepslate.fallacy.survive.client.screen

import com.github.wintersteve25.tau.menu.TauMenuHelper
import dev.deepslate.fallacy.survive.ModMenus
import dev.deepslate.fallacy.survive.TheMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(modid = TheMod.ID, value = [Dist.CLIENT])
object MenuScreensHandler {
    @SubscribeEvent
    fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
        TauMenuHelper.registerMenuScreen(event, ModMenus.BOIL_POT_MENU)
    }
}
