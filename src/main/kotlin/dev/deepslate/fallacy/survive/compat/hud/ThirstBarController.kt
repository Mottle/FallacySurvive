package dev.deepslate.fallacy.survive.compat.hud

import dev.deepslate.fallacy.hud.client.StatusBarUI
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.utils.RGB
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

class ThirstBarController : StatusBarUI.Controller {
    override val priority: Int = 2

    override val status: StatusBarUI.Status
        get() {
            val cap = entity?.getCapability(ModCapabilities.THIRST)

            val max = cap?.max ?: -1
            val value = cap?.value ?: -1

            return StatusBarUI.Status(value, max)
        }

    override val color: RGB = RGB.from(0x1e90ff)

    override val icon: ResourceLocation = TheMod.withID("thirst")

    override fun shouldRender(entity: Entity): Boolean {
        //只有玩家显示饱食度
        if (entity is Player) {
            return !(entity.isSpectator || entity.abilities.instabuild)
        }

        return false
    }
}