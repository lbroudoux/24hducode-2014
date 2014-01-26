package org.les24hducode.supercal.fmw.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.les24hducode.supercal.fmw.domain.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Test case for TripRepository
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TripRepositoryTest {

   @Autowired
   Neo4jTemplate template;
   
   @Autowired
   TripRepository tripRepository;
   
   @Test
   @Transactional
   public void testFinders() {
      Trip trip = new Trip();
      trip.setId("myTrip");
      trip.setHeadSign("head sign");
      template.save(trip);
      
      // KO !?
      Trip result = tripRepository.findTripFromId("myTrip");
      System.err.println(result);
      
      // KO !?
      result = tripRepository.findByPropertyValue("id", "myTrip");
      System.err.println(result);
      
      // OK.
      result = tripRepository.getTripById("myTrip");
      assertNotNull(result);
      assertEquals("myTrip", result.getId());
      assertEquals(trip.getNodeId(), result.getNodeId());
   }

}
