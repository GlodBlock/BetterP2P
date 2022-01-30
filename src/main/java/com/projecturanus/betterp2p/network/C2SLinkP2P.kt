package com.projecturanus.betterp2p.network

import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf

class C2SLinkP2P(var input: Int = -1, var output: Int = -1): IMessage {
    override fun fromBytes(buf: ByteBuf) {
        input = buf.readInt()
        output = buf.readInt()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(input)
        buf.writeInt(output)
    }
}
