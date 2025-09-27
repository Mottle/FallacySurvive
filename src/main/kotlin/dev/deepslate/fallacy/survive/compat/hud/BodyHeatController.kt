package dev.deepslate.fallacy.survive.compat.hud

import dev.deepslate.fallacy.hud.client.CenterHudUI
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class BodyHeatController : CenterHudUI.Controller {
    override val icon: ResourceLocation
        get() = TheMod.withID("heat")

    override val component: Component?
        get() {
            val player = Minecraft.getInstance().cameraEntity as? Player ?: return null
            val heat = getBodyHeat(player)
            val celsius = heat - ThermodynamicsEngine.FREEZING_POINT
            val text = String.format("%.1f", celsius)
            return Component.literal("$text°C")
        }

    private fun getBodyHeat(entity: Entity): Float {
        val cap = entity.getCapability(ModCapabilities.BODY_HEAT) ?: return -1f

        return cap.heat
    }

    override fun shouldRender(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        //只有玩家显示体温 - TODO: 添加其他生物的体温显示
        if (entity is Player) {
            return !(entity.isSpectator || entity.abilities.instabuild)
        }

        return false
    }
}