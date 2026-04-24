package dev.deepslate.fallacy.survive.menu

import com.github.wintersteve25.tau.components.base.DynamicUIComponent
import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.components.inventory.ItemSlot
import com.github.wintersteve25.tau.components.inventory.PlayerInventory
import com.github.wintersteve25.tau.components.layout.Align
import com.github.wintersteve25.tau.components.layout.Column
import com.github.wintersteve25.tau.components.layout.Row
import com.github.wintersteve25.tau.components.utils.Container
import com.github.wintersteve25.tau.components.utils.Text
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.layout.LayoutSetting
import com.github.wintersteve25.tau.menu.TauContainerMenu
import com.github.wintersteve25.tau.menu.UIMenu
import com.github.wintersteve25.tau.menu.handlers.ItemSlotHandler
import com.github.wintersteve25.tau.menu.handlers.PlayerInventoryHandler
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.Variable
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.*

object BoilPotUI : UIMenu {
    override fun build(layout: Layout, theme: Theme, menu: TauContainerMenu): UIComponent {
        val entity = menu.getBlockEntity(BoilPotEntity::class.java).orElse(null)

        return object : DynamicUIComponent() {
            private var lastHeat = Int.MIN_VALUE

            override fun build(l: Layout, t: Theme): UIComponent {
                val text =
                    if (lastHeat == Int.MIN_VALUE) "--.- C" else String.format(Locale.ROOT, "%.1f C", lastHeat / 10f)

                val title = Text.Builder(Component.translatable("block.fallacy_survive.copper_boil_pot"))
                val heatLabel = Text.Builder(Component.literal("Internal: $text"))

                val potSlots = if (entity != null) {
                    Row.Builder().withAlignment(LayoutSetting.START).withSpacing(0).build(
                        (0 until entity.inventory.slots).map {
                            ItemSlot(ItemSlotHandler(entity.inventory, it, Variable(true)))
                        }
                    )
                } else {
                    Text.Builder(Component.literal("..."))
                }

                val playerInv = PlayerInventory(Variable(true))

                return Container.Builder().withChild(
                    Align.Builder()
                        .withHorizontal(LayoutSetting.CENTER)
                        .withVertical(LayoutSetting.CENTER)
                        .build(
                            Column.Builder().withSpacing(6).build(title, heatLabel, potSlots, playerInv)
                        )
                )
            }

            override fun tick() {
                super.tick()
                val current = menu.getGetterForDataSlot("heat")
                    .map { it.get() }
                    .orElse(Int.MIN_VALUE)
                if (current != lastHeat) {
                    lastHeat = current
                    rebuild()
                }
            }
        }
    }

    override fun getSize(): com.github.wintersteve25.tau.utils.SimpleVec2i =
        com.github.wintersteve25.tau.utils.SimpleVec2i(176, 166)

    override fun getTitle(): Component = Component.translatable("block.fallacy_survive.copper_boil_pot")

    override fun getSlots(menu: TauContainerMenu): List<com.github.wintersteve25.tau.menu.handlers.ISlotHandler> {
        val entity = menu.getBlockEntity(BoilPotEntity::class.java).orElse(null)
            ?: return emptyList()
        val potSlots = (0 until entity.inventory.slots).map { slotIndex ->
            ItemSlotHandler(entity.inventory, slotIndex, Variable(true))
        }
        return potSlots + PlayerInventoryHandler(Variable(true))
    }

    override fun addDataSlots(menu: TauContainerMenu) {
        val entity = menu.getBlockEntity(BoilPotEntity::class.java).orElse(null) ?: return
        menu.addDataSlot(
            "heat",
            { ((entity.heat - ThermodynamicsEngine.FREEZING_POINT) * 10f).toInt() },
            { }
        )
    }

    override fun quickMoveStack(menu: TauContainerMenu, player: Player, index: Int): ItemStack {
        val slot = menu.getSlot(index)
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY
        val original = slot.item
        if (index < 6) {
            if (!menu.moveItemStackTo(original, 6, 42, true)) return ItemStack.EMPTY
        } else {
            return ItemStack.EMPTY
        }
        if (original.isEmpty) {
            slot.set(ItemStack.EMPTY)
        } else {
            slot.setChanged()
        }
        return slot.item.copy()
    }

    override fun stillValid(menu: TauContainerMenu, player: Player): Boolean {
        val entity = menu.getBlockEntity(BoilPotEntity::class.java).orElse(null) ?: return false
        return entity.blockPos.distSqr(player.blockPosition()) <= 64.0
    }
}
