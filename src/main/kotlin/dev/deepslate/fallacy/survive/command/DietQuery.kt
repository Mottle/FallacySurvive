package dev.deepslate.fallacy.survive.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.network.packet.DisplayDietPacket
import dev.deepslate.fallacy.utils.command.GameCommand
import dev.deepslate.fallacy.utils.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.server.ServerLifecycleHooks

data object DietQuery : GameCommand {
    override val source: String = "fallacy diet query %s<name>"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> =
        mapOf("name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME)

    override val permissionRequired: String? = "fallacy_survive.command.diet.query"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = StringArgumentType.getString(context, "name")
        val player = ServerLifecycleHooks.getCurrentServer()?.playerList?.getPlayerByName(playerName)

        if (player == null) {
            context.source.sendFailure(Component.literal("Player not found"))
            return Command.SINGLE_SUCCESS
        }

        val diet = player.getCapability(ModCapabilities.DIET)!!
        val data = diet.nutrition
        PacketDistributor.sendToPlayer(player, DisplayDietPacket(data))

        return Command.SINGLE_SUCCESS
    }
}