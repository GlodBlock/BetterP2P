package com.projecturanus.betterp2p.network

import com.projecturanus.betterp2p.item.ItemAdvancedMemoryCard
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext

class ServerUpdateInfoHandler : IMessageHandler<C2SUpdateInfo, IMessage?> {
    override fun onMessage(message: C2SUpdateInfo, ctx: MessageContext): IMessage? {
        val player = ctx.serverHandler.playerEntity
        val stack = player.heldItem
        if (stack.item is ItemAdvancedMemoryCard) {
            ItemAdvancedMemoryCard.writeInfo(stack, message.info)
        }
        return null
    }
}
