package com.projecturanus.betterp2p.network

import com.projecturanus.betterp2p.util.p2p.P2PCache
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent
import net.minecraftforge.common.MinecraftForge

object ServerPlayerDisconnectHandler {

    @SubscribeEvent
    fun onLoggedOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        P2PCache.statusMap.remove(event.player.uniqueID)
    }

    fun register() {
        val handler = ServerPlayerDisconnectHandler
        MinecraftForge.EVENT_BUS.register(handler)
        FMLCommonHandler.instance().bus().register(handler)
    }
}
