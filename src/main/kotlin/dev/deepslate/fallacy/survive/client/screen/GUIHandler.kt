package dev.deepslate.fallacy.survive.client.screen

import dev.deepslate.fallacy.base.client.screen.UIContext
import dev.deepslate.fallacy.base.client.screen.component.ExtendedUIRender
import dev.deepslate.fallacy.base.client.screen.component.vanilla.VanillaDynamicButton
import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ScreenEvent


@EventBusSubscriber(modid = TheMod.ID, value = [Dist.CLIENT])
object GUIHandler {
    @SubscribeEvent
    fun onGuiInit(event: ScreenEvent.Init.Post) {
        val screen = event.screen as? InventoryScreen ?: return
        val on = TheMod.withID("on")
        val off = TheMod.withID("off")
        val sprites = WidgetSprites(off, on)
        val context = UIContext().generateNext(screen)

        val button = VanillaDynamicButton(screen, screen.guiLeft + 126, screen.height / 2 - 22, 20, 18, sprites) { _ ->
            Minecraft.getInstance().setScreen(ExtendedUIRender(DietUI().withContext(context), isPause = false))
        }
        event.addListener(button)
    }
}