package org.les24hducode.supercal.fmw.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * Domain object representing a GTFS Route.
 * @author laurent
 */
@RooJavaBean
@RooToString
@NodeEntity
public class Route {

   @GraphId
   private Long nodeId;
   
   // Fields coming from GTFS.
   private String id;
   private String shortName;
   private String longName;
   private String description;
   private String routeType;
   
   @Fetch
   @RelatedTo(type = "ROUTE_TRIPS", direction = Direction.BOTH)
   private Set<Trip> trips = new HashSet<Trip>();
}
