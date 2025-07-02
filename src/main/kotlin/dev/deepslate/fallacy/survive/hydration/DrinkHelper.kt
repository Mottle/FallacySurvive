package dev.deepslate.fallacy.survive.hydration

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.hydration.entity.Thirst
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.HitResult

object DrinkHelper {

    private const val INTERVAL_TICKS = 10

    private fun checkIntervalAndUpdateLastDrinkTick(level: Level, player: Player): Boolean {
        val lastDrinkTick = player.getData(ModAttachments.LAST_DRINK_TICK_STAMP)
        val tick = if (level.isClientSide) TickCollector.clientTickCount else level.server!!.tickCount

        if (tick - lastDrinkTick >= INTERVAL_TICKS) {
            player.setData(ModAttachments.LAST_DRINK_TICK_STAMP, tick)
            return true
        }

        return false
    }

    fun attemptDrink(level: Level, player: Player): InteractionResult {
        val hit = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY)

        if (hit.type != HitResult.Type.BLOCK) return InteractionResult.PASS

        val pos = hit.blockPos
        val state = if (level.isAreaLoaded(pos, 0)) level.getBlockState(pos) else Blocks.AIR.defaultBlockState()
        val fluid = state.fluidState.type

        if (!fluid.isSame(Fluids.WATER)) return InteractionResult.PASS
        if (!checkIntervalAndUpdateLastDrinkTick(level, player)) return InteractionResult.FAIL

        val thirst = player.getCapability(ModCapabilities.THIRST)!!

        if (thirst.value >= thirst.max) return InteractionResult.PASS
        if (!level.isClientSide) {
            doDrink(level, player, thirst, state, pos)
        }

        return InteractionResult.SUCCESS
    }

    fun doDrink(level: Level, player: Player, thirst: Thirst, state: BlockState, pos: BlockPos) {
        thirst.drink(1f)
        level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0f, 1.0f)
    }
}