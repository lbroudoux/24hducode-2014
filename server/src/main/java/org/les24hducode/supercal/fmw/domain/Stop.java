package org.les24hducode.supercal.fmw.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * 
 * @author laurent
 */
@RooJavaBean
@RooToString
public class Stop {

   private String id;
   private String name;
   private String description;
   private Double latitude;
   private Double longitude;
   private String url;
   private String locationType;
   private String parentStation; 
}
