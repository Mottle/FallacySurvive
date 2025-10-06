package dev.deepslate.fallacy.survive.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class BoilPot(properties: Properties) : Block(properties) {

    companion object {
        private fun makeShape(): VoxelShape {
            var shape = Shapes.empty()
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 0.8125), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.125, 0.875, 0.125, 0.875), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.1875, 0.9375), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 0.1875, 0.75, 0.9375), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.8125, 0.1875, 0.0625, 0.9375, 0.75, 0.9375), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.1875, 0.0625, 0.8125, 0.75, 0.1875), BooleanOp.OR)
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.1875, 0.8125, 0.8125, 0.75, 0.9375), BooleanOp.OR)

            return shape
        }

        @JvmStatic
        val SHAPE = makeShape()
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE
}