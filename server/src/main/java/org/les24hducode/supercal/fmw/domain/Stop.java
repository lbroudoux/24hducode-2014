package org.les24hducode.supercal.fmw.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.impl.traversal.TraversalDescriptionImpl;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphTraversal;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.core.FieldTraversalDescriptionBuilder;
import org.springframework.data.neo4j.mapping.Neo4jPersistentProperty;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * The representation of a GTFS Stop.
 * @author laurent
 */
@RooJavaBean
@RooToString
@NodeEntity
public class Stop {

   @GraphId
   private Long nodeId;
   
   // Fields coming from GTFS.
   private String id;
   private String name;
   private String description;
   private Double latitude;
   private Double longitude;
   private String url;
   private String locationType;
   private String parentStation;
   
   // De-normalized fields for computation.
   private Long routeId;
   
   @Fetch
   @RelatedTo(type = "ROUTE", direction = Direction.BOTH)
   private Set<Stop> routeStops = new HashSet<Stop>();
   
   /*
   @RelatedToVia
   private List<Section> sections;
   
   private Section leadTo(Stop end, String routeId){
      Section section = new Section(this, end, routeId);
      sections.add(section);
      return section;
   }
   */
   
   @GraphTraversal(traversal = SectionTraversalBuilder.class, elementClass = Stop.class, params = "SECTION")
   private Iterable<Stop> sectionStops;

   private static class SectionTraversalBuilder implements FieldTraversalDescriptionBuilder {
       @Override
       public TraversalDescription build(Object start, Neo4jPersistentProperty field, String... params) {
          return new TraversalDescriptionImpl()
             .relationships(DynamicRelationshipType.withName(params[0])).depthFirst();
       }
   }
   
   @Indexed(indexType = IndexType.POINT, indexName = "stopLocation")
   String wkt;
   public void setLocation(double lon, double lat) {
      this.wkt = String.format("POINT( %.3f %.3f )",lon,lat).replace(',', '.');
   }
}
