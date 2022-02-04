package com.projecturanus.betterp2p

import com.projecturanus.betterp2p.client.render.RenderHandler
import com.projecturanus.betterp2p.config.BetterP2PConfig
import com.projecturanus.betterp2p.item.ItemAdvancedMemoryCard
import com.projecturanus.betterp2p.network.ModNetwork
import com.projecturanus.betterp2p.network.ServerPlayerDisconnectHandler
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.Logger

const val MODID = "betterp2p"

/**
 * Better P2P is created by LasmGratel.
 * GlodBlock backported this to 1.7.10.
 */
@Mod(modid = MODID, version = "GRADLETOKEN_VERSION", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", dependencies = "required-after: appliedenergistics2; required-after: forgelin;")
object BetterP2P {
    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        ModNetwork.registerNetwork()
        BetterP2PConfig.loadConfig(Configuration(event.suggestedConfigurationFile))
        GameRegistry.registerItem(ItemAdvancedMemoryCard, "advanced_memory_card", MODID)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        ServerPlayerDisconnectHandler.register()
        RenderHandler.register()
        GameRegistry.addRecipe(ShapelessRecipes(ItemStack(ItemAdvancedMemoryCard), listOf(
            GameRegistry.findItemStack("appliedenergistics2", "item.ToolNetworkTool", 1),
            GameRegistry.findItemStack("appliedenergistics2", "item.ToolMemoryCard", 1)
        )))
    }
}
