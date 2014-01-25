package org.les24hducode.supercal.fmw.repository;

import org.les24hducode.supercal.fmw.domain.Trip;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author laurent
 */
@Repository
public interface TripRepository extends GraphRepository<Trip>{

   @Query("start trip=node:Trip(id={0}) return trip")
   public Trip findTripFromId(String id);
}
