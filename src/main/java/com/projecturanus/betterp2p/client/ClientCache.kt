package com.projecturanus.betterp2p.client

import net.minecraft.util.EnumFacing

object ClientCache {
    val positions = mutableListOf<Pair<List<Int?>, EnumFacing>>()
    var selectedPosition: List<Int?>? = null
    var selectedFacing: EnumFacing? = null

    fun clear() {
        positions.clear()
        selectedPosition = null
        selectedFacing = null
    }
}
