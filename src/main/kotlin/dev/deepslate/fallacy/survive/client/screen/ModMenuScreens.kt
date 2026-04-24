package dev.deepslate.fallacy.survive.client.screen

import com.github.wintersteve25.tau.menu.TauMenuHelper
import dev.deepslate.fallacy.survive.menu.ModMenus
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

object ModMenuScreens {
    fun registerScreens(event: RegisterMenuScreensEvent) {
        TauMenuHelper.registerMenuScreen(event, ModMenus.BOIL_POT)
    }
}
