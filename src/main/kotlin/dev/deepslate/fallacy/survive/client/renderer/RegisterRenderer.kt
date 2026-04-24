package dev.deepslate.fallacy.survive.client.renderer

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.block.ModBlockEntities
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber(modid = TheMod.ID, value = [Dist.CLIENT])
object RegisterRenderer {

    @SubscribeEvent
    fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(ModBlockEntities.BOIL_POT.get(), ::BoilPotBlockEntityRenderer)
    }
}
