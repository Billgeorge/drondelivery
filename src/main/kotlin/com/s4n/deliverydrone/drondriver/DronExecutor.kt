package com.s4n.deliverydrone.drondriver

import com.s4n.deliverydrone.model.Position
import com.s4n.deliverydrone.util.CardinalPoint
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

    private fun goForwardPosition(position: Position) {
        when (position.direction) {
            CardinalPoint.E -> {
                position.x = position.x + 1
            }
            CardinalPoint.N -> {
                position.y = position.y + 1
            }
            CardinalPoint.S -> {
                position.y = position.y - 1
            }
            CardinalPoint.W -> {
                position.x = position.x - 1
            }
        }
    }

    private fun turnPosition(position: Position, isLeft: Boolean) {
        when (position.direction) {
            CardinalPoint.E -> {
                position.direction = if (isLeft) {
                    CardinalPoint.N
                } else CardinalPoint.S
            }
            CardinalPoint.N -> {
                position.direction = if (isLeft) {
                    CardinalPoint.W
                } else CardinalPoint.E
            }
            CardinalPoint.S -> {
                position.direction = if (isLeft) {
                    CardinalPoint.E
                } else CardinalPoint.W
            }
            CardinalPoint.W -> {
                position.direction = if (isLeft) {
                    CardinalPoint.S
                } else CardinalPoint.N
            }
        }
    }
}