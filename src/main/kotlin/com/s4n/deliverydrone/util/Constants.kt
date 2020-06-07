package com.s4n.deliverydrone.util

import com.s4n.deliverydrone.drondriver.*
import com.s4n.deliverydrone.model.Position

/**
 * This value should be in a parametric table, in this way It can be changed without deploy again
 * Some of them can be part of propeerties (for sending form execution)
 */
const val MAXIMUM_NUMBER_DELIVERIES = 3
const val MAXIMUM_NUMBER_BLOCKS = 10
const val NUMBER_OF_DRONS = 20
const val REPORT_PATH= "reports"
const val TITLE_REPORT= "== Delivery Report =="
const val INSTRUCTIONS_PATH = "instructions"


enum class Commands(val command: Command) {
    A(GoForwardCommand),
    I(TurnLeftCommand),
    D(TurnRightCommand)
}

enum class CardinalPoint(val cardinalName: String) {
    N("North"),
    S("South"),
    E("East"),
    W("West")
}

/**
 * define differents types of Dron, this enum should be part of the request, but in this case we only have default Dron
 */
enum class DronType(val dronExecutor: DronExecutor) {
    DEFAULT(DefaultDronExecutor)
}

val INITIAL_DEFAULT_POSITION = Position(0, 0, CardinalPoint.N)