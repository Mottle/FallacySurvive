package dev.deepslate.fallacy.survive.hydration.entity

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.hydration.logic.DrinkHelper
import dev.deepslate.fallacy.survive.network.packet.DrinkInWorldPacket
import dev.deepslate.fallacy.survive.network.packet.ThirstSyncPacket
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.block.AnvilBlock
import net.minecraft.world.level.block.BarrelBlock
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import net.neoforged.neoforge.network.PacketDistributor

@EventBusSubscriber(modid = TheMod.ID)
object Handler {
    const val BAST_TICK_RATE = 20

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(BAST_TICK_RATE)) return

        val player = event.entity as? ServerPlayer ?: return
        val thirst = player.getCapability(ModCapabilities.THIRST)!!

        thirst.tick()
    }

    //喝水
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    fun onPlayerRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        val level = event.level
        val state = level.getBlockState(event.pos)
        val stack = event.itemStack
        val hand = event.hand
        val player = event.entity

        //必须左右是都是空手
        if (!player.mainHandItem.isEmpty || !player.offhandItem.isEmpty) return
        if (!player.isShiftKeyDown) return
        if (stack.isEmpty && !event.isCanceled && hand == InteractionHand.MAIN_HAND) {
            val useBlockResult = state.useItemOn(stack, level, player, hand, event.hitVec)
            if (useBlockResult.consumesAction()) {
                if (player is ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, event.pos, stack)
                }
                event.isCanceled = true
                event.cancellationResult = useBlockResult.result()
            }
        } else {
            val result = DrinkHelper.attemptDrink(level, player)
            if (result != InteractionResult.PASS) {
                event.isCanceled = true
                event.cancellationResult = InteractionResult.FAIL
            }

            if (result.shouldSwing()) player.swing(InteractionHand.MAIN_HAND, true)
        }

        if (state.block is AnvilBlock || state.block is BarrelBlock) event.useBlock = TriState.TRUE
    }

    //只在Client Side触发，遂向Server发送DrinkInWorldPacket
    @SubscribeEvent
    fun onPlayerRightClickEmpty(event: PlayerInteractEvent.RightClickEmpty) {
        if (event.side.isServer) return

        val player = event.entity

        if (event.hand != InteractionHand.MAIN_HAND) return
        //必须左右是都是空手
        if (!player.offhandItem.isEmpty || !player.mainHandItem.isEmpty) return
        if (!player.isShiftKeyDown) return

        val result = DrinkHelper.attemptDrink(event.level, player)

        if (result != InteractionResult.SUCCESS) return
        PacketDistributor.sendToServer(DrinkInWorldPacket.PACKET)
    }

    //从末地归来复制数据
    @SubscribeEvent
    fun onPlayerDataClone(event: PlayerEvent.Clone) {
        val origin = event.original
        if (!event.isWasDeath) {
            val player = event.entity
            val oldThirst = origin.getData(ModAttachments.THIRST)
            val oldDrinkTicks = origin.getData(ModAttachments.LAST_DRINK_TICK_STAMP)

            player.setData(ModAttachments.THIRST, oldThirst)
            player.setData(ModAttachments.LAST_DRINK_TICK_STAMP, oldDrinkTicks)
        }
    }

    //玩家登录时同步一次数据
    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as ServerPlayer
        val cap = player.getCapability(ModCapabilities.THIRST)!!
        PacketDistributor.sendToPlayer(player, ThirstSyncPacket(cap.value))
    }
}