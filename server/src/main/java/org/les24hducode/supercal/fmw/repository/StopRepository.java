package org.les24hducode.supercal.fmw.repository;

import org.les24hducode.supercal.fmw.domain.Stop;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.SpatialRepository;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author laurent
 */
@Repository
public interface StopRepository extends GraphRepository<Stop>, SpatialRepository<Stop>{
	
	public Stop getStopById(String id);

   @Query("start stop=node:Stop(id={0}) return stop")
   public Stop findStopFromId(String id);
}
