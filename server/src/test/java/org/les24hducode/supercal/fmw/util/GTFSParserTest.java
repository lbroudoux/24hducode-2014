package org.les24hducode.supercal.fmw.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.les24hducode.supercal.fmw.domain.Route;
import org.les24hducode.supercal.fmw.domain.Stop;
import org.les24hducode.supercal.fmw.domain.Trip;
import org.les24hducode.supercal.fmw.repository.RouteRepository;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.les24hducode.supercal.fmw.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Test case for GTFSParser.
 * @author laurent
 */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class GTFSParserTest{

   @Autowired
   private Neo4jTemplate template;
   
   @Autowired
   private RouteRepository routeRepository;
   @Autowired
   private StopRepository stopRepository;
   @Autowired
   private TripRepository tripRepository;
   
   private GTFSParser parser;
   
   @Before
   public void setUp(){
      parser = new GTFSParser(template);
   }
   
   @Test
   @Transactional
   public void testParseGTFS() throws IOException{
      // Parse routes file.
      System.err.println("Starting to parse routes file...");
      File routeFile = new File("target/test-classes/gtfs/routes.txt");
      parser.parseGTFSRoutes(routeFile);
      
      List<Route> routes = template.findAll(Route.class).as(List.class);
      assertEquals(98, routes.size());
      
      // Parse trips file.
      System.err.println("Starting to parse trips file...");
      File tripFile = new File("target/test-classes/gtfs/trips.txt");
      parser.parseGTFSTrips(tripFile, routeRepository);
      
      List<Trip> trips = template.findAll(Trip.class).as(List.class);
      assertEquals(23395, trips.size());
      
      // Parse stops file.
      System.err.println("Starting to parse stops file...");
      File stopFile = new File("target/test-classes/gtfs/stops.txt");
      parser.parseGTFSStops(stopFile, routeRepository);
      
      List<Stop> stops = template.findAll(Stop.class).as(List.class);
      assertEquals(1427, stops.size());
      
      // Parse stop_times files.
      System.err.println("Starting to parse stop_times file...");
      File stopTimeFile = new File("target/test-classes/gtfs/stop_times.txt");
      parser.parseGTFSStopTimes(stopTimeFile, tripRepository, stopRepository);
      
      /*
      List<Stop> stops = template.findAll(Stop.class).as(List.class);
      assertEquals(1427, stops.size());
      */
   }

}
