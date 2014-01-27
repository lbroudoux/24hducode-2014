package org.les24hducode.supercal.fmw.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * Section is a relationship between 2 stops of the same Route.
 * @author laurent
 */
@RooJavaBean
@RooToString
@RelationshipEntity(type = "SECTION")
public class Section {

   @GraphId
   private Long id;
   
   @StartNode
   private Stop start;
   
   @EndNode
   private Stop end;
   
   private String routeId;
   
   public Section(){
   }
   
   public Section(Stop start, Stop end, String routeId){
      this.start = start;
      this.end = end;
      this.routeId = routeId;
   }
}
