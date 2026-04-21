package dev.deepslate.fallacy.survive.client.screen

import com.github.wintersteve25.tau.components.base.DynamicUIComponent
import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.components.layout.Center
import com.github.wintersteve25.tau.components.layout.Column
import com.github.wintersteve25.tau.components.layout.Row
import com.github.wintersteve25.tau.components.utils.Container
import com.github.wintersteve25.tau.components.utils.Padding
import com.github.wintersteve25.tau.components.utils.Sized
import com.github.wintersteve25.tau.components.utils.Text
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.Pad
import com.github.wintersteve25.tau.utils.Size
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import java.util.*
import kotlin.math.roundToInt

class BoilPotUI(private val boilPotPos: BlockPos) : DynamicUIComponent() {
    private var lastHeatDeci = resolveHeatDeciCelsius(boilPotPos)
    private var cachedText = formatHeatText(lastHeatDeci)
    private var pollTicks = 0

    override fun build(layout: Layout, theme: Theme): UIComponent {
        val title = Padding(
            Pad.Builder().top(8).build(),
            Center(Text.Builder(Component.translatable("block.fallacy_survive.copper_boil_pot")))
        )

        val temperature = Center(Text.Builder(Component.literal(cachedText)))

        val closeButton = Center(
            Text.Builder(Component.literal("Press inventory key to close"))
        )

        val panel = Container.Builder().withChild(
            Column.Builder().withSpacing(12).build(title, temperature, closeButton)
        )

        return Center(
            Sized(
                Size.staticSize(220, 110),
                Row.Builder().build(panel)
            )
        )
    }

    private fun resolveHeatDeciCelsius(pos: BlockPos): Int {
        val level = Minecraft.getInstance().level ?: return Int.MIN_VALUE
        val blockEntity = level.getBlockEntity(pos) as? BoilPotEntity ?: return Int.MIN_VALUE
        return ((blockEntity.heat - ThermodynamicsEngine.FREEZING_POINT) * 10f).roundToInt()
    }

    private fun formatHeatText(heatDeci: Int): String {
        return if (heatDeci == Int.MIN_VALUE) {
            "Internal: --.- C"
        } else {
            String.format(Locale.ROOT, "Internal: %.1f C", heatDeci / 10f)
        }
    }

    override fun tick() {
        super.tick()
        val needFastProbe = lastHeatDeci == Int.MIN_VALUE
        if (!needFastProbe) {
            pollTicks++
            if (pollTicks < 5) return
            pollTicks = 0
        }

        val heatDeci = resolveHeatDeciCelsius(boilPotPos)
        if (heatDeci == lastHeatDeci) return

        lastHeatDeci = heatDeci
        cachedText = formatHeatText(heatDeci)
        rebuild()
    }
}
