package dev.deepslate.fallacy.survive.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.utils.command.GameCommand
import dev.deepslate.fallacy.utils.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.server.ServerLifecycleHooks

data object ThirstQuery : GameCommand {
    override val source: String = "fallacy thirst query %s<name>"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> =
        mapOf("name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME)

    override val permissionRequired: String? = "fallacy_survive.command.thirst.query"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = context.getArgument("name", String::class.java)
        val player = ServerLifecycleHooks.getCurrentServer()?.playerList?.getPlayerByName(playerName)

        if (player == null) {
            context.source.sendFailure(Component.literal("Player not found"))
            return Command.SINGLE_SUCCESS
        }

        val thirst = player.getCapability(ModCapabilities.THIRST)!!.value
        context.source.sendSuccess({ Component.literal("$playerName Thirst: $thirst") }, false)

        return Command.SINGLE_SUCCESS
    }
}