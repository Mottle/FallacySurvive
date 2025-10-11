package dev.deepslate.fallacy.survive.block

import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class BoilPotBlock(properties: Properties) : BaseEntityBlock(properties) {

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

        val FILLED: BooleanProperty = BooleanProperty.create("filled")
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FILLED, false))
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FILLED)
    }

//    override fun useWithoutItem(
//        state: BlockState,
//        level: Level,
//        pos: BlockPos,
//        player: Player,
//        hitResult: BlockHitResult
//    ): InteractionResult {
//        if (level.isClientSide) return InteractionResult.SUCCESS
//
//        return InteractionResult.CONSUME
//    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (stack.`is`(Items.WATER_BUCKET)) return handleWaterBucket(stack, state, level, pos, player, hand, hitResult)
        if (stack.`is`(Items.BUCKET)) return super.useItemOn(stack, state, level, pos, player, hand, hitResult)

        val itemCap = stack.getCapability(Capabilities.FluidHandler.ITEM)

        if (itemCap != null) return handleTank(itemCap, stack, state, level, pos, player, hand, hitResult)

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
    }

    private fun handleWaterBucket(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!level.isClientSide) {
            val blockCap = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, hitResult.direction)
            val filled = blockCap?.fill(FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE) ?: 0

            if (filled > 0) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ItemStack(Items.BUCKET)))
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun handleTank(
        itemCap: IFluidHandler,
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!level.isClientSide) {
            val blockCap = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, hitResult.direction)
            val fluid = itemCap.drain(1000, IFluidHandler.FluidAction.EXECUTE)
            val filled = blockCap?.fill(fluid, IFluidHandler.FluidAction.EXECUTE) ?: 0

            fluid.amount -= filled
            if (filled > 0) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun codec(): MapCodec<out BaseEntityBlock?> = simpleCodec(::BoilPotBlock)

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        ModBlockEntities.BOIL_POT.create(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>):
            BlockEntityTicker<T>? {
        if (level.isClientSide) return null
        return createTickerHelper(blockEntityType, ModBlockEntities.BOIL_POT.get(), BoilPotEntity::serverTick)
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (!oldState.`is`(this)) level.invalidateCapabilities(pos)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        if (!state.`is`(newState.block)) level.invalidateCapabilities(pos)
    }
}