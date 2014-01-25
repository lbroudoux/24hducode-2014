package org.les24hducode.supercal.fmw;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.les24hducode.supercal.fmw.domain.Stop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Basic test case for neo4j persistence.
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Neo4jTest {

   @Autowired
   Neo4jTemplate template;
   
   @Test
   @Transactional
   public void testBasicPersistenceAndRetrieval() {
      // Create and save a stop.
      Stop stop = new Stop();
      stop.setId("id");
      stop.setName("name");
      stop.setDescription("description");
      template.save(stop);
      
      Stop result = template.findOne(stop.getNodeId(), Stop.class);
      assertNotNull(stop.getNodeId());
      assertEquals(result.getNodeId(), stop.getNodeId());
      assertEquals("name", stop.getName());
      System.err.println("Persisted nodeId: " + stop.getNodeId());
   }

}
