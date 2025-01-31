package com.lambda.client.command.commands

import com.lambda.client.command.ClientCommand
import com.lambda.client.util.text.MessageSendHelper.sendChatMessage
import com.lambda.client.util.text.formatValue
import net.minecraft.entity.Entity

object VanishCommand : ClientCommand(
    name = "vanish",
    description = "Allows you to vanish using an entity."
) {
    private var vehicle: Entity? = null

    init {
        executeSafe {
            if (player.ridingEntity != null && vehicle == null) {
                vehicle = player.ridingEntity?.also {
                    player.dismountRidingEntity()
                    world.removeEntityFromWorld(it.entityId)
                    sendChatMessage("Vehicle " + formatValue(it.name) + " ID: " + formatValue(it.entityId) + " dismounted and removed.")
                }
            } else {
                vehicle?.let {
                    it.isDead = false
                    world.addEntityToWorld(it.entityId, it)
                    player.startRiding(it, true)
                    vehicle = null
                    sendChatMessage("Vehicle " + formatValue(it.name) + " ID: " + formatValue(it.entityId) + " created and mounted.")
                } ?: sendChatMessage("Not riding any vehicles.")
            }
        }
    }
}