package dev.deepslate.fallacy.survive.menu

import com.github.wintersteve25.tau.components.base.DynamicUIComponent
import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.components.inventory.ItemSlot
import com.github.wintersteve25.tau.components.inventory.PlayerInventory
import com.github.wintersteve25.tau.components.layout.Center
import com.github.wintersteve25.tau.components.layout.Row
import com.github.wintersteve25.tau.components.layout.Stack
import com.github.wintersteve25.tau.components.utils.Container
import com.github.wintersteve25.tau.components.utils.Padding
import com.github.wintersteve25.tau.components.utils.Sized
import com.github.wintersteve25.tau.components.utils.Text
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.menu.TauContainerMenu
import com.github.wintersteve25.tau.menu.UIMenu
import com.github.wintersteve25.tau.menu.handlers.ISlotHandler
import com.github.wintersteve25.tau.menu.handlers.ItemSlotHandler
import com.github.wintersteve25.tau.menu.handlers.PlayerInventoryHandler
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.Pad
import com.github.wintersteve25.tau.utils.SimpleVec2i
import com.github.wintersteve25.tau.utils.Size
import com.github.wintersteve25.tau.utils.Variable
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.*
import kotlin.math.roundToInt

class BoilPotUI : UIMenu {
    companion object {
        private val SIZE = SimpleVec2i(176, 166)
    }

    private val slotsEnabled = Variable(true)
    private var syncedHeatDeciKelvin = ThermodynamicsEngine.MIN_HEAT * 10

    private val fallbackInventory = object : ItemStackHandler(BoilPotEntity.INVENTORY_SLOTS) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = false

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = stack
    }

    override fun build(layout: Layout, theme: Theme, menu: TauContainerMenu): UIComponent {
        val inventory = boilPotInventory(menu)
        val slotHandlers = boilPotSlotHandlers(inventory)
        val slotComponents = slotHandlers.map(::ItemSlot)

        val boilPotSlotsRow = Row.Builder().withSpacing(0).build(slotComponents)
        val title = Padding(
            Pad.Builder().left(8).top(6).build(),
            Row.Builder().withSpacing(8).build(
                Text.Builder(Component.translatable("block.fallacy_survive.copper_boil_pot")),
                InternalTemperatureLabel()
            )
        )

        val slotsLayer = Padding(
            Pad.Builder().left(7).top(17).build(),
            boilPotSlotsRow
        )

        val playerInventoryLayer = Padding(
            Pad.Builder().left(7).top(83).build(),
            PlayerInventory(slotsEnabled)
        )

        val panel = Stack.Builder().build(
            title,
            slotsLayer,
            playerInventoryLayer
        )

        return Center(Sized(Size.staticSize(SIZE), Container.Builder().withChild(panel)))
    }

    override fun getSize(): SimpleVec2i = SIZE

    override fun getTitle(): Component = Component.translatable("block.fallacy_survive.copper_boil_pot")

    override fun addDataSlots(menu: TauContainerMenu) {
        menu.addDataSlot(
            "boil_pot_heat_deci_kelvin",
            { currentHeatDeciKelvin(menu) },
            { value -> syncedHeatDeciKelvin = value.coerceAtLeast(ThermodynamicsEngine.MIN_HEAT * 10) }
        )
    }

    override fun getSlots(menu: TauContainerMenu): List<ISlotHandler> {
        val inventory = boilPotInventory(menu)
        return buildList {
            addAll(boilPotSlotHandlers(inventory))
            add(PlayerInventoryHandler(slotsEnabled))
        }
    }

    override fun quickMoveStack(menu: TauContainerMenu, player: Player, index: Int): ItemStack {
        val source = menu.slots.getOrNull(index) ?: return ItemStack.EMPTY
        if (!source.hasItem()) return ItemStack.EMPTY

        val sourceStack = source.item
        val sourceCopy = sourceStack.copy()

        val boilPotSlots = 0 until BoilPotEntity.INVENTORY_SLOTS
        val playerSlots = BoilPotEntity.INVENTORY_SLOTS until menu.slots.size

        if (index in boilPotSlots) {
            if (!menu.moveItemStackTo(sourceStack, playerSlots.first, playerSlots.last + 1, false)) {
                return ItemStack.EMPTY
            }
        } else {
            return ItemStack.EMPTY
        }

        if (sourceStack.isEmpty) source.set(ItemStack.EMPTY)
        else source.setChanged()

        source.onTake(player, sourceStack)
        return sourceCopy
    }

    private fun boilPotInventory(menu: TauContainerMenu): IItemHandler {
        return menu.getBlockEntity(BoilPotEntity::class.java).map { it.inventory }.orElse(fallbackInventory)
    }

    private fun currentHeatDeciKelvin(menu: TauContainerMenu): Int {
        return menu.getBlockEntity(BoilPotEntity::class.java).map {
            (it.heat * 10f).roundToInt()
        }.orElse(ThermodynamicsEngine.MIN_HEAT * 10)
    }

    private fun internalTemperatureText(): String {
        val celsius = syncedHeatDeciKelvin / 10f - ThermodynamicsEngine.FREEZING_POINT
        return String.format(Locale.ROOT, "Internal: %.1f C", celsius)
    }

    private inner class InternalTemperatureLabel : DynamicUIComponent() {
        override fun build(layout: Layout, theme: Theme): UIComponent {
            return Text.Builder(Component.literal(internalTemperatureText()))
        }

        override fun tick() {
            super.tick()
            rebuild()
        }
    }

    private fun boilPotSlotHandlers(inventory: IItemHandler): List<ItemSlotHandler> {
        return List(BoilPotEntity.INVENTORY_SLOTS) { index -> ItemSlotHandler(inventory, index, slotsEnabled) }
    }
}
