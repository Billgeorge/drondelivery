import com.s4n.deliverydrone.drondriver.*
import com.s4n.deliverydrone.model.Position
import com.s4n.deliverydrone.util.CardinalPoint
import com.s4n.deliverydrone.util.INITIAL_DEFAULT_POSITION
import com.s4n.deliverydrone.util.MAXIMUM_NUMBER_BLOCKS
import kotlin.math.sqrt

fun goForwardPosition(position: Position) {
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

fun turnPosition(position: Position, isLeft: Boolean) {
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