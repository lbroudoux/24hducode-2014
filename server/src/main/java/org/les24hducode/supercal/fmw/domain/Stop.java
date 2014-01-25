package org.les24hducode.supercal.fmw.domain;

import java.util.Set;

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
public class Stop {

   @GraphId
   private Long nodeId;
   
   private String id;
   private String name;
   private String description;
   private Double latitude;
   private Double longitude;
   private String url;
   private String locationType;
   private String parentStation;
   
   /*
   @Fetch
   @RelatedTo(type = "ROUTE", direction = Direction.BOTH)
   private Set<Stop> routeStops;
   */
}
