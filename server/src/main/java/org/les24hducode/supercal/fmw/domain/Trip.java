package org.les24hducode.supercal.fmw.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * 
 * @author laurent
 */
@RooJavaBean
@RooToString
@NodeEntity
public class Trip {

   @GraphId
   private Long nodeId;
   
   // Fields coming from GTFS.
   private String id;
   private String headSign;
   
   @Fetch
   @RelatedTo(type = "ROUTE_TRIPS", direction = Direction.BOTH)
   private Route route;
}
