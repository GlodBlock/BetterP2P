package com.projecturanus.betterp2p.client.render;

import com.projecturanus.betterp2p.client.ClientCache;
import com.projecturanus.betterp2p.item.ItemAdvancedMemoryCard;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;


public class RenderHandler {

    @SubscribeEvent
    public void renderOverlays(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ItemAdvancedMemoryCard.INSTANCE) {
            if (!ClientCache.INSTANCE.getPositions().isEmpty() || ClientCache.INSTANCE.getSelectedPosition() != null) {
                if (ClientCache.INSTANCE.getSelectedPosition() != null) {
                    List<Pair<List<Integer>, EnumFacing>> tmp = new ArrayList<>();
                    tmp.add(new Pair<>(ClientCache.INSTANCE.getSelectedPosition(), ClientCache.INSTANCE.getSelectedFacing()));
                    OutlineRenderer.renderOutlinesWithFacing(
                        event,
                        player,
                        tmp,
                        0x45,
                        0xDA,
                        0x75
                    );
                }
                OutlineRenderer.renderOutlinesWithFacing(event, player, ClientCache.INSTANCE.getPositions(), 0x66, 0xCC, 0xFF);
            }
        }
    }

    public static void register() {
        RenderHandler handler = new RenderHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }
}
