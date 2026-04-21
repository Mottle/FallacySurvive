package dev.deepslate.fallacy.survive.block.entity

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import dev.deepslate.fallacy.utils.minutes2Ticks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.items.ItemStackHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BoilPotEntity(type: BlockEntityType<*>, val pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(BoilPotEntity::class.java)

        const val INVENTORY_SLOTS = 6

        const val TICK_INTERVAL = 20

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: BoilPotEntity) {
            if (level.isClientSide) return
            if (!TickCollector.checkServerTickInterval(TICK_INTERVAL)) return

            if (entity.heat <= 0.1f) {
                val localHeat = ThermodynamicsEngine.getHeat(level, pos)
                entity.heat = localHeat.toFloat()
            }

            if (entity.content.isEmpty) return

            if (entity.heat >= ThermodynamicsEngine.fromFreezingPoint(100)) {
                entity.boiledTicks += TICK_INTERVAL
                if (entity.boiledTicks >= minutes2Ticks(5)) {
                    entity.content.amount -= 200
                    entity.concentration = (entity.concentration + 1).coerceAtMost(4)
                }
            } else {
                entity.boiledTicks = 0
            }
        }
    }

    var content: FluidStack = FluidStack.EMPTY

    var heat: Float = 0f

    var boiledTicks = 0

    var concentration = 0

    val inventory: ItemStackHandler = object : ItemStackHandler(INVENTORY_SLOTS) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = false

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = stack

        override fun onContentsChanged(slot: Int) {
            setChanged()
        }
    }

    fun dropInventory(level: Level, pos: BlockPos) {
        for (slot in 0 until inventory.slots) {
            val stack = inventory.getStackInSlot(slot)
            if (stack.isEmpty) continue
            Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack.copy())
        }
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        FluidStack.CODEC.encodeStart(NbtOps.INSTANCE, content).ifSuccess { nbt ->
            tag.put("content", nbt)
        }.ifError {
            LOGGER.warn("Failed to save content for boil pot at $pos: $it")
        }

        tag.putFloat("heat", heat)
        tag.putInt("boiled_ticks", boiledTicks)
        tag.putInt("concentration", concentration)
        tag.put("inventory", inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        tag.get("content")?.let { tag ->
            FluidStack.CODEC.parse(NbtOps.INSTANCE, tag).ifSuccess { stack -> content = stack }.ifError {
                LOGGER.warn("Failed to load content for boil pot at $pos: $it")
            }
        }

        heat = tag.getFloat("heat")
        boiledTicks = tag.getInt("boiled_ticks")
        concentration = tag.getInt("concentration")

        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"))
        }
    }

    fun getDisplayName(): Component = Component.translatable("block.fallacy_survive.copper_boil_pot")
}
