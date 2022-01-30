package com.projecturanus.betterp2p.network

import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.util.EnumFacing

fun readInfo(buf: ByteBuf): P2PInfo {
    val index = buf.readInt()
    val freq = buf.readLong()
    val posX = buf.readInt()
    val posY = buf.readInt()
    val posZ = buf.readInt()
    val facing = EnumFacing.values()[buf.readInt()]
    return P2PInfo(index, freq, posX, posY, posZ, facing, buf.readBoolean(), buf.readBoolean())
}

fun writeInfo(buf: ByteBuf, info: P2PInfo) {
    buf.writeInt(info.index)
    buf.writeLong(info.frequency)
    buf.writeInt(info.posX)
    buf.writeInt(info.posY)
    buf.writeInt(info.posZ)
    buf.writeInt(info.facing.ordinal)
    buf.writeBoolean(info.output)
    buf.writeBoolean(info.hasChannel)
}

class S2CRefreshInfo(var infos: List<P2PInfo> = emptyList()) : IMessage {
    override fun fromBytes(buf: ByteBuf) {
        val length = buf.readInt()
        val list = ArrayList<P2PInfo>(length)
        for (i in 0 until length) {
            list += readInfo(buf)
        }
        infos = list
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(infos.size)
        for (info in infos) {
            writeInfo(buf, info)
        }
    }
}
