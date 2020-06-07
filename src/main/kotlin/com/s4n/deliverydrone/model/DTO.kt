package com.s4n.deliverydrone.model

import com.s4n.deliverydrone.util.CardinalPoint

data class Position(var x: Long, var y: Long, var direction: CardinalPoint)
data class DeliveryState(val dron:Dron, val message:String)