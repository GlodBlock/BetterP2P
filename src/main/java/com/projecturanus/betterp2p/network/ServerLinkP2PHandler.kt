package com.projecturanus.betterp2p.network

import com.projecturanus.betterp2p.util.p2p.P2PCache
import com.projecturanus.betterp2p.util.p2p.getInfo
import com.projecturanus.betterp2p.util.p2p.linkP2P
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext

class ServerLinkP2PHandler : IMessageHandler<C2SLinkP2P, IMessage?> {
    override fun onMessage(message: C2SLinkP2P, ctx: MessageContext): IMessage? {
        if (!P2PCache.statusMap.containsKey(ctx.serverHandler.playerEntity.uniqueID)) return null
        val status = P2PCache.statusMap[ctx.serverHandler.playerEntity.uniqueID]!!

        val result = linkP2P(ctx.serverHandler.playerEntity, message.input, message.output, status)
        if (result != null) {
            status.listP2P[message.input] = result.first
            status.listP2P[message.output] = result.second
            ModNetwork.channel.sendTo(S2CRefreshInfo(listOf(result.first.getInfo(message.input), result.second.getInfo(message.output))), ctx.serverHandler.playerEntity)
        }
        return null
    }
}
