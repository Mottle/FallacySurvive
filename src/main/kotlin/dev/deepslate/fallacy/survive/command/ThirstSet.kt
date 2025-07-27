package dev.deepslate.fallacy.survive.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.utils.command.GameCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

data object ThirstSet : GameCommand {
    override val source: String = "fallacy thirst set %f<value>"
    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = "fallacy_survive.command.thirst.set"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player ?: return 0
        val thirst = context.getArgument("value", Float::class.java)
        player.getCapability(ModCapabilities.THIRST)!!.value = thirst
        context.source.sendSuccess({ Component.literal("Thirst set to $thirst") }, false)

        return Command.SINGLE_SUCCESS
    }
}