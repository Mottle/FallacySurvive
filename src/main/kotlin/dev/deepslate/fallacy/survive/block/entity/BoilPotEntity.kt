package dev.deepslate.fallacy.survive.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BoilPotEntity(type: BlockEntityType<*>, val pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(BoilPotEntity::class.java)

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: BoilPotEntity) {}
    }

    var content: FluidStack = FluidStack.EMPTY

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        FluidStack.CODEC.encodeStart(NbtOps.INSTANCE, content).ifSuccess { nbt ->
            tag.put("content", nbt)
        }.ifError {
            LOGGER.warn("Failed to save content for boil pot at $pos: $it")
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        tag.get("content")?.let { tag ->
            FluidStack.CODEC.parse(NbtOps.INSTANCE, tag).ifSuccess { stack -> content = stack }.ifError {
                LOGGER.warn("Failed to load content for boil pot at $pos: $it")
            }
        }
    }
}