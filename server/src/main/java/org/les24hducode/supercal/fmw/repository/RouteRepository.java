package org.les24hducode.supercal.fmw.repository;

import org.les24hducode.supercal.fmw.domain.Route;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author laurent
 */
@Repository
public interface RouteRepository extends GraphRepository<Route>{

   @Query("start route=node:Route(id={0}) return route")
   public Route findRouteFromId(String id);
}
