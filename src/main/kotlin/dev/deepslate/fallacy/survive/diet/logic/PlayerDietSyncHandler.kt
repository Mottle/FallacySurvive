package dev.deepslate.fallacy.survive.diet.logic

//@EventBusSubscriber(modid = TheMod.ID)
//object PlayerDietSyncHandler {
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    fun onPlayerTickLast(event: PlayerTickEvent.Post) {
//        if (!TickCollector.checkServerTickInterval(seconds2Ticks(20))) return
//
//        val player = event.entity as? ServerPlayer ?: return
//        val diet = player.getCapability(ModCapabilities.DIET) as? LivingEntityDiet ?: return
//
//        if (diet.changed) diet.synchronize()
//    }
//}