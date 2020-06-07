package com.s4n.deliverydrone.drondriver

import com.s4n.deliverydrone.model.Position

interface Command {
    fun execute(dronExecutor: DronExecutor, position: Position)
}

object GoForwardCommand : Command {
    override fun execute(dronExecutor: DronExecutor, position: Position) {
        dronExecutor.goForward(position)
    }
}

object TurnLeftCommand : Command{
    override fun execute(dronExecutor: DronExecutor, position: Position) {
        dronExecutor.turnLeft(position)
    }
}
object TurnRightCommand : Command{
    override fun execute(dronExecutor: DronExecutor, position: Position) {
        dronExecutor.turnRight(position)
    }
}

object DeliveryCommand : Command{
    override fun execute(dronExecutor: DronExecutor, position: Position) {
        dronExecutor.delivery()
    }
}