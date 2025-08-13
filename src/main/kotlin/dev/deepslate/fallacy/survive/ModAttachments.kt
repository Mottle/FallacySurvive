package dev.deepslate.fallacy.survive

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.survive.diet.entity.FoodHistory
import dev.deepslate.fallacy.survive.diet.entity.LivingEntityNutritionState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModAttachments {
    @JvmStatic
    private val registry = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheMod.ID)

    @JvmStatic
    val THIRST: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("thirst") { _ ->
        AttachmentType.builder { _ -> 20f }.serialize(Codec.FLOAT).build()
    }

    //用于记录玩家上一次喝水的时间戳，客户端和服务端各自维护一个LAST_DRINK_TICK，不进行同步
    @JvmStatic
    internal val LAST_DRINK_TICK_STAMP: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
        registry.register("last_drink_tick_stamp") { _ -> AttachmentType.builder { _ -> -1 }.build() }

    @JvmStatic
    val NUTRITION_STATE: DeferredHolder<AttachmentType<*>, AttachmentType<LivingEntityNutritionState>> =
        registry.register("nutrition_state") { _ ->
            AttachmentType.builder { _ -> LivingEntityNutritionState.DEFAULT }
                .serialize(LivingEntityNutritionState.CODEC).copyOnDeath().build()
        }

    //用于记录最近吃过的食物
    @JvmStatic
    val FOOD_HISTORY: DeferredHolder<AttachmentType<*>, AttachmentType<FoodHistory>> =
        registry.register("food_history") { _ ->
            AttachmentType.builder { _ -> FoodHistory() }.serialize(FoodHistory.CODEC).copyOnDeath().build()
        }

    //用于记录玩家最近5秒的奔跑水平距离
//    @JvmStatic
//    internal val LAST_5S_SPRINT_DISTANCE: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
//        registry.register("last_5s_sprint_distance") { _ ->
//            AttachmentType.builder { _ -> 0f }.serialize(Codec.FLOAT).build()
//        }

    @JvmStatic
    //记录热敏感体(生物、具有热交换能力的方块)的热量
    val HEAT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("heat") { _ ->
        AttachmentType.builder { _ -> 0f }.serialize(Codec.FLOAT).build()
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModLoadCompleted(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}