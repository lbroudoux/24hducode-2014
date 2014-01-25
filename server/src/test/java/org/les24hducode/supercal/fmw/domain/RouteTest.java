package org.les24hducode.supercal.fmw.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Test case for Route domain objects.
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RouteTest {

   @Autowired
   Neo4jTemplate template;
   
   @Test
   @Transactional
   public void testRouteTripsRelation(){
      // Create route.
      Route route = new Route();
      route.setShortName("Route name");
      route.setLongName("Route long name");
      route.setDescription("Route description");
      
      // Create trip.
      Trip trip = new Trip();
      trip.setId("myTrip");
      
      // Assign trip to route.
      route.getTrips().add(trip);
      
      // Save and retrieve then assert.
      template.save(route);
      Route result = template.findOne(route.getNodeId(), Route.class);
      
      assertNotNull(result.getNodeId());
      assertNotNull(result.getTrips());
      assertFalse(result.getTrips().isEmpty());
      assertEquals(1, result.getTrips().size());
      
      Trip first = route.getTrips().iterator().next();
      assertNotNull(first.getNodeId());
      assertEquals("myTrip", first.getId());
      
      // Bi-directionnal relations are not fetched from leave point of view.
      assertNull(first.getRoute());
      
      // You should request another time using leave as first request node.
      Trip find = template.findOne(first.getNodeId(), Trip.class);
      assertNotNull(find.getRoute());
      assertEquals(result.getNodeId(), find.getRoute().getNodeId());
   }

}
