package org.les24hducode.supercal.fmw.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.javabean.RooJavaBean;
/**
 * Path representes an itinerary to a destination. It contains 
 * segments of routes.
 * @author laurent
 */
@RooJavaBean
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Path")
public class PathView {

   @XmlElement(name = "departure_time", required = true)
   private String departureTime; 
   
   @XmlElement(name = "departure_latitude", required = true)
   private Double departureLatitude;
   
   @XmlElement(name = "departure_longitude", required = true)
   private Double departureLongitude;
   
   @XmlElement(name = "arrival_time", required = true)
   private String arrivalTime;
   
   @XmlElement(name = "routes", required = false)
   public List<RouteView> routes = new ArrayList<RouteView>();
}
