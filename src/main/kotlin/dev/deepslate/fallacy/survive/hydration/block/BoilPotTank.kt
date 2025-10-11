package dev.deepslate.fallacy.survive.hydration.block

import dev.deepslate.fallacy.survive.block.BoilPotBlock
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.min

class BoilPotTank(val level: Level, val pos: BlockPos) : IFluidHandler {

    companion object {
        const val CAPACITY = 1000
    }

    private val entity = level.getBlockEntity(pos) as? BoilPotEntity

    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack = when (tank) {
        0 -> entity?.content ?: FluidStack.EMPTY
        else -> FluidStack.EMPTY
    }

    override fun getTankCapacity(tank: Int): Int = when (tank) {
        0 -> CAPACITY
        else -> 0
    }

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
        return true
    }

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (entity == null) return 0

        if (resource.isEmpty || !isFluidValid(0, resource)) {
            return 0
        }

        if (action.simulate()) {
            if (entity.content.isEmpty) {
                return min(CAPACITY, resource.amount)
            }

            if (!FluidStack.isSameFluidSameComponents(entity.content, resource)) {
                return 0
            }

            return min(CAPACITY - entity.content.amount, resource.amount)
        }

        if (entity.content.isEmpty) {
            entity.content = resource.copyWithAmount(min(CAPACITY, resource.amount))
            onContentsChanged()
            return entity.content.amount
        }

        if (!FluidStack.isSameFluidSameComponents(entity.content, resource)) {
            return 0
        }

        var filled: Int = CAPACITY - entity.content.amount

        if (resource.amount < filled) {
            entity.content.grow(resource.amount)
            filled = resource.amount
        } else {
            entity.content.amount = CAPACITY
        }

        if (filled > 0) onContentsChanged()

        return filled
    }

    private fun onContentsChanged() {
        if (entity == null) return
        if (entity.content.amount > 0) {
            level.setBlockAndUpdate(pos, entity.blockState.setValue(BoilPotBlock.FILLED, true))
        }
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (entity == null) return FluidStack.EMPTY
        if (resource.isEmpty || !FluidStack.isSameFluidSameComponents(resource, entity.content)) {
            return FluidStack.EMPTY
        }
        return drain(resource.amount, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (entity == null) return FluidStack.EMPTY

        val fluid = entity.content
        var drained = maxDrain

        if (fluid.amount < drained) {
            drained = fluid.amount
        }

        val stack: FluidStack = fluid.copyWithAmount(drained)

        if (action.execute() && drained > 0) {
            fluid.shrink(drained)
            onContentsChanged()
        }

        return stack
    }
}