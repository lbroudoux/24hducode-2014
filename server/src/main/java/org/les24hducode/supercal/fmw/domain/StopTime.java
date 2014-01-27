package org.les24hducode.supercal.fmw.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Domain object representing a GTFS StopTime.
 * @author laurent
 */
@RooJavaBean
@RooToString
@NodeEntity
public class StopTime {

   @GraphId
   private Long nodeId;
   
   // Fields coming from GTFS.
   private int arrivalTime;
   private int departureTime;
   private int stopSequence;
   private int pickupType;
   private int dropOffType;
}
