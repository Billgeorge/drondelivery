package com.s4n.deliverydrone.repository

import com.s4n.deliverydrone.model.Dron
import com.s4n.deliverydrone.model.Route

/**
 * object Repositories for entities
 */
object DronRepository : CrudRepositoryImpl<Dron>()
object RouteRepository : CrudRepositoryImpl<Route>(){

    fun getRoutesByDronId(dronId:Long):List<Route>{
        return list.filter { it.dronId==dronId && it.initialPosition==it.finalPosition }
    }
}