package com.projecturanus.betterp2p.item

import appeng.api.networking.IGridHost
import appeng.core.CreativeTab
import appeng.parts.p2p.PartP2PTunnel
import com.projecturanus.betterp2p.MODID
import com.projecturanus.betterp2p.capability.MemoryInfo
import com.projecturanus.betterp2p.client.ClientCache
import com.projecturanus.betterp2p.network.ModNetwork
import com.projecturanus.betterp2p.network.S2CListP2P
import com.projecturanus.betterp2p.util.getPart
import com.projecturanus.betterp2p.util.p2p.P2PCache
import com.projecturanus.betterp2p.util.p2p.P2PStatus
import com.projecturanus.betterp2p.util.p2p.getInfo
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

object ItemAdvancedMemoryCard : Item() {
    init {
        maxStackSize = 1
        unlocalizedName = "advanced_memory_card"
        creativeTab = CreativeTab.instance
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected)
    }

    private fun sendStatus(status: P2PStatus, info: MemoryInfo, player: EntityPlayerMP) {
        P2PCache.statusMap[player.uniqueID] = status
        ModNetwork.channel.sendTo(
            S2CListP2P(status.listP2P.mapIndexed { index, p2p -> p2p.getInfo(index) }, info),
            player
        )
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<Any?>, boolean: Boolean) {
        val info = getInfo(stack)
        tooltip += StatCollector.translateToLocal("gui.advanced_memory_card.mode.${info.mode.name.lowercase(Locale.getDefault())}")
    }

    @SideOnly(Side.CLIENT)
    private fun clearClientCache() {
        ClientCache.clear()
    }

    override fun onItemRightClick(itemstack: ItemStack, worldIn: World, playerIn: EntityPlayer): ItemStack {
        if (playerIn.isSneaking && worldIn.isRemote) {
            clearClientCache()
        }
        return super.onItemRightClick(itemstack, worldIn, playerIn)
    }

    override fun onItemUse(itemstack: ItemStack, player: EntityPlayer, w: World, x: Int, y: Int, z: Int, side: Int, hx: Float, hy: Float, hz: Float): Boolean {
        if (!w.isRemote) {
            val te = w.getTileEntity(x, y, z)
            if (te is IGridHost && te.getGridNode(ForgeDirection.getOrientation(side)) != null) {
                val part = getPart(w, x, y, z, hx, hy, hz)
                val stack = player.heldItem
                val info = getInfo(stack)
                if (part is PartP2PTunnel<*>) {
                    val status = P2PStatus(player, 0, part.gridNode.grid, part)
                    info.selectedIndex = status.listP2P.indexOfFirst { it == status.targetP2P }
                    writeInfo(stack, info)

                    sendStatus(status, info, player as EntityPlayerMP)
                    return true
                } else {
                    val node = te.getGridNode(ForgeDirection.getOrientation(side))!!
                    info.selectedIndex = -1
                    writeInfo(stack, info)
                    sendStatus(P2PStatus(player, 0, node.grid), info, player as EntityPlayerMP)
                    return true
                }
            }
        }
        return false
    }

    override fun doesSneakBypassUse(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?): Boolean {
        return true
    }

    @SideOnly(Side.CLIENT)
    override fun registerIcons(ri: IIconRegister) {
        itemIcon = ri.registerIcon("$MODID:advanced_memory_card")
    }

    private fun getInfo(stack: ItemStack): MemoryInfo {
        if (stack.item != this) throw ClassCastException("Cannot cast ${stack.item.javaClass.name} to ${javaClass.name}")

        if (stack.tagCompound == null) stack.tagCompound = NBTTagCompound()
        val compound = stack.tagCompound!!
        if (!compound.hasKey("selectedIndex")) compound.setInteger("selectedIndex", -1)

        return MemoryInfo(compound.getInteger("selectedIndex"), compound.getLong("frequency"), BetterMemoryCardModes.values()[compound.getInteger("mode")])
    }

    fun writeInfo(stack: ItemStack, info: MemoryInfo) {
        if (stack.item != this) throw ClassCastException("Cannot cast ${stack.item.javaClass.name} to ${javaClass.name}")

        if (stack.tagCompound == null) stack.tagCompound = NBTTagCompound()
        val compound = stack.tagCompound!!
        compound.setInteger("selectedIndex", info.selectedIndex)
        compound.setLong("frequency", info.frequency)
        compound.setInteger("mode", info.mode.ordinal)
    }
}
