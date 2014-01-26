package org.les24hducode.supercal.fmw;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.les24hducode.supercal.fmw.domain.Stop;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Basic test case for neo4j geo services
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Neo4jGeoTest {

   @Autowired
   Neo4jTemplate template;
   
   @Autowired
   StopRepository stopRepository;
   
   @Test
   @Transactional
   public void testGeoServices(){
      
      // Create and save a stop.
      Stop stop = new Stop();
      stop.setId("id");
      stop.setName("name");
      stop.setDescription("description");
      stop.setLocation(47.996953, 0.206299);
      template.save(stop);
      
      Stop stop2 = new Stop();
      stop2.setId("id2");
      stop2.setName("name2");
      stop2.setDescription("description2");
      stop2.setLocation(48.5, -2.2);
      template.save(stop2);
      
      // Le Mans is less than 25 km from Parigne le Polin => one stop as result.
      Iterable<Stop> stops = stopRepository.findWithinDistance("stopLocation", 47.843257, 0.119781, 25);
      for (Stop result : stops) {
         assertEquals(stop.getNodeId(), result.getNodeId());
      }
      // Le Mans is more than 15 km from Parigne le Polin => one stop as result.
      stops = stopRepository.findWithinDistance("stopLocation", 47.843257, 0.119781, 15);
      assertFalse(stops.iterator().hasNext());
   }
}
