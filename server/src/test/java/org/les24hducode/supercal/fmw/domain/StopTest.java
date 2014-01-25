package org.les24hducode.supercal.fmw.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Test case for Stop domain object.
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StopTest {

   private Route route;
   
   @Autowired
   Neo4jTemplate template;
   
   @Before
   public void  setUp(){
      route = new Route();
      route.setShortName("Route name");
      route.setLongName("Route long name");
      route.setDescription("Route description");
      template.save(route);
   }
   
//   @Test
//   @Transactional
//   public void testStopRelations(){
//      // Create Stop.
//      Stop stop = new Stop();
//      stop.setId("myStop");
//      stop.setLatitude(47.110);
//      stop.setLongitude(-1.66);
//      stop.setRouteId(route.getNodeId());
//      
//      // Create other associated stops.
//      Stop stop1 = new Stop();
//      stop1.setId("myStop1");
//      stop1.setLatitude(47.110);
//      stop1.setLongitude(-1.661);
//      stop1.setRouteId(route.getNodeId());
//      
//      Stop stop2 = new Stop();
//      stop2.setId("myStop2");
//      stop2.setLatitude(47.110);
//      stop2.setLongitude(-1.661);
//      stop2.setRouteId(route.getNodeId());
//      
//      stop.getRouteStops().add(stop1);
//      stop1.getRouteStops().add(stop2);
//      
//      // Save and retrieve then assert.
//      template.save(stop);
//      Stop result = template.findOne(stop.getNodeId(), Stop.class);
//      
//      assertNotNull(result.getNodeId());
//      assertNotNull(result.getRouteStops());
//      assertFalse(result.getRouteStops().isEmpty());
//      assertEquals(1, result.getRouteStops().size());
//      
//      // Check that loaded node has correct relations.
//      Stop first = result.getRouteStops().iterator().next();
//      assertEquals(2, first.getRouteStops().size());
//      
//      // Reload releated node and check its relations. 
//      Stop result1 = template.findOne(stop1.getNodeId(), Stop.class);
//      
//      assertFalse(result1.getRouteStops().isEmpty());
//      assertEquals(2, result1.getRouteStops().size());
//   }
   
   @Test
   @Transactional
   public void testStopSections(){
      // Create Stop.
      Stop stop = new Stop();
      stop.setId("myStop");
      stop.setLatitude(47.110);
      stop.setLongitude(-1.66);
      stop.setRouteId(route.getNodeId());
      
      // Create other associated stops.
      Stop stop1 = new Stop();
      stop1.setId("myStop1");
      stop1.setLatitude(47.110);
      stop1.setLongitude(-1.661);
      stop1.setRouteId(route.getNodeId());
      
      Stop stop2 = new Stop();
      stop2.setId("myStop2");
      stop2.setLatitude(47.110);
      stop2.setLongitude(-1.661);
      stop2.setRouteId(route.getNodeId());
      
      Stop stop3 = new Stop();
      stop3.setId("myStop3");
      stop3.setLatitude(47.110);
      stop3.setLongitude(-1.661);
      stop3.setRouteId(route.getNodeId());
      
      Stop stop4 = new Stop();
      stop4.setId("myStop4");
      stop4.setLatitude(47.110);
      stop4.setLongitude(-1.661);
      stop4.setRouteId(route.getNodeId());
      
      
      template.save(stop);
      template.save(stop1);
      template.save(stop2);
      template.save(stop3);
      template.save(stop4);
      
      Section section = template.createRelationshipBetween(stop, stop1, Section.class, "SECTION", true);
      Section section1 = template.createRelationshipBetween(stop1, stop3, Section.class, "SECTION", true);
      Section section11 = template.createRelationshipBetween(stop1, stop4, Section.class, "SECTION", true);
      
      assertEquals(stop.getNodeId(), section.getStart().getNodeId());
      assertEquals(stop1.getNodeId(), section.getEnd().getNodeId());
      
      Iterable<Stop> results = stop.getSectionStops();
      System.err.println("stop: " + stop.getNodeId());
      System.err.println("stop1: " + stop1.getNodeId());
      System.err.println("stop3: " + stop3.getNodeId());
      System.err.println("stop4: " + stop4.getNodeId());
      for (Stop result : results) {
         System.err.println(result.getNodeId());
      }
      
      /*
      TraversalDescription traversalDescription = Traversal.description()
            .depthFirst()
            .relationships(DynamicRelationshipType.withName("SECTION"))
            .evaluator(Evaluators.toDepth(5));
      for (Path position : traversalDescription.traverse(stop)) {
         System.out.println("Path from start node to current position is " + position);
      }
      */
   }
}
