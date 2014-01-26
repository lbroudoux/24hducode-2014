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
 * 
 * @author laurent
 */
@RooJavaBean
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Route")
public class RouteView {

   @XmlElement(name = "short_name", required = true)
   private String shortName;
   
   @XmlElement(name = "long_name", required = true)
   private String longName;
   
   @XmlElement(name = "description", required = true)
   private String description;
   
   @XmlElement(name = "stopTimes", required = false)
   public List<StopTimeView> stopTimes = new ArrayList<StopTimeView>();
}
