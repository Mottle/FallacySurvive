package dev.deepslate.fallacy.survive.block.entity

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import dev.deepslate.fallacy.utils.minutes2Ticks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.items.ItemStackHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.abs
import kotlin.math.min

class BoilPotEntity(type: BlockEntityType<*>, val pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(BoilPotEntity::class.java)

        const val INVENTORY_SLOTS = 6

        const val TICK_INTERVAL = 20

        private const val EMPTY_HEAT_UP_RATE_PER_SECOND = 60f
        private const val EMPTY_COOL_DOWN_RATE_PER_SECOND = 30f
        private const val FILLED_HEAT_UP_RATE_PER_SECOND = 20f
        private const val FILLED_COOL_DOWN_RATE_PER_SECOND = 10f
        private const val HEAT_SYNC_THRESHOLD = 0.5f
        private const val HEAT_SYNC_INTERVAL_TICKS = 20 * 5

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: BoilPotEntity) {
            if (level.isClientSide) return
            if (!TickCollector.checkServerTickInterval(TICK_INTERVAL)) return

            val oldConcentration = entity.concentration
            val oldContentAmount = entity.content.amount

            val localHeat = ThermodynamicsEngine.getHeat(level, pos)
            updateHeatTowardsEnvironment(entity, localHeat.toFloat())

            if (!entity.content.isEmpty) {
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

            entity.ticksSinceLastHeatSync += TICK_INTERVAL

            val nonHeatStateChanged =
                entity.concentration != oldConcentration ||
                        entity.content.amount != oldContentAmount

            val heatDeltaSinceLastSync = abs(entity.heat - entity.lastSyncedHeat)
            val significantHeatChange = heatDeltaSinceLastSync >= HEAT_SYNC_THRESHOLD
            val heatSyncWindowReached = entity.ticksSinceLastHeatSync >= HEAT_SYNC_INTERVAL_TICKS
            val shouldSyncHeat = significantHeatChange || heatSyncWindowReached

            if (nonHeatStateChanged || shouldSyncHeat) {
                entity.setChanged()
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS)
                entity.lastSyncedHeat = entity.heat
                entity.ticksSinceLastHeatSync = 0
            }
        }

        private fun updateHeatTowardsEnvironment(entity: BoilPotEntity, targetHeat: Float) {
            val secondsPerStep = TICK_INTERVAL / 20f
            val isFilled = !entity.content.isEmpty
            val heatUpRate = if (isFilled) FILLED_HEAT_UP_RATE_PER_SECOND else EMPTY_HEAT_UP_RATE_PER_SECOND
            val coolDownRate = if (isFilled) FILLED_COOL_DOWN_RATE_PER_SECOND else EMPTY_COOL_DOWN_RATE_PER_SECOND
            val maxStep = if (targetHeat >= entity.heat) heatUpRate * secondsPerStep else coolDownRate * secondsPerStep

            val delta = targetHeat - entity.heat
            val clampedDelta = if (delta > 0f) min(delta, maxStep) else -min(-delta, maxStep)

            if (clampedDelta == 0f) return
            entity.heat += clampedDelta
        }

    }

    var content: FluidStack = FluidStack.EMPTY

    var heat: Float = 0f

    var boiledTicks = 0

    var concentration = 0

    private var lastSyncedHeat = 0f

    private var ticksSinceLastHeatSync = 0

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

        lastSyncedHeat = heat
        ticksSinceLastHeatSync = 0
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(registries)

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    fun getDisplayName(): Component = Component.translatable("block.fallacy_survive.copper_boil_pot")
}
