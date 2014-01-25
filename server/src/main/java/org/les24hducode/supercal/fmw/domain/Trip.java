package org.les24hducode.supercal.fmw.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
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
   
   private String id;
   private String headSign;
}
