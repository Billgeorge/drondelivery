package com.s4n.deliverydrone.drondriver

import com.s4n.deliverydrone.model.Position
import goForwardPosition
import turnPosition
import java.util.logging.Logger

/**
 * With this layer the behavior of the Dron can change. This means there can exist differents type of Drons
 * Each Dron has its own way of calculate position. IN this case one A command is one block forward, but for other this
 * same command could be 2 blocks forward
 */
interface DronExecutor {

    fun goForward(position: Position)

    fun turnLeft(position: Position)

    fun turnRight(position: Position)

    fun delivery()
}

object DefaultDronExecutor : DronExecutor {

    private val log: Logger = Logger.getLogger(DefaultDronExecutor::javaClass.name)

    override fun goForward(position: Position) {
        log.info("executing goForward")
        // In real case position should be received from Dron
        goForwardPosition(position)
    }

    override fun turnLeft(position: Position) {
        log.info("executing turnLeft 90 grades Instruction")
        turnPosition(position, true)
    }

    override fun turnRight(position: Position) {
        log.info("executing turnRight 90 grades Instruction")
        turnPosition(position, false)
    }

    override fun delivery() {
        log.info("executing delivery")
    }
}