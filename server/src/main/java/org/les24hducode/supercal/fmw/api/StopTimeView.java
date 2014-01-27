package org.les24hducode.supercal.fmw.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.javabean.RooJavaBean;
/**
 * An aggregate condensing information from GTFS Stop and StopTime.
 * @author laurent
 */
@RooJavaBean
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "StopTime")
public class StopTimeView {

   @XmlElement(name = "name", required = true)
   private String name;
   
   @XmlElement(name = "arrival_time", required = true)
   private String arrivalTime;
   
   @XmlElement(name = "latitude", required = true)
   private Double latitude;
   
   @XmlElement(name = "longitude", required = true)
   private Double longitude;
}
