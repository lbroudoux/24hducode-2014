package org.les24hducode.supercal.fmw.web;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffPCSCodes;
import org.les24hducode.supercal.fmw.repository.RouteRepository;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.les24hducode.supercal.fmw.repository.TripRepository;
import org.les24hducode.supercal.fmw.util.GTFSParser;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * @author laurent
 */
@Controller
@RequestMapping("/loaddata")
public class LoaddataController {

   /** A commons logger for diagnostic messages. */
   private static Log log = LogFactory.getLog(LoaddataController.class);
   
   @Autowired
   private Neo4jTemplate template;
   
   @Autowired
   private RouteRepository routeRepository;
   @Autowired
   private StopRepository stopRepository;
   @Autowired
   private TripRepository tripRepository;
   
   
   @RequestMapping(value = "/", method = RequestMethod.GET)
   public String load(
         ModelMap modelMap) throws Exception{
    
      log.info("In load()");
      
      // Build a new GTFS Parser.
      GTFSParser parser = new GTFSParser(template);
      
      // Load routes...
      Transaction tx = template.getGraphDatabaseService().beginTx();
      log.info("Starting to parse routes file...");
      try{
         File routeFile = new File("target/test-classes/gtfs/routes.txt");
         parser.parseGTFSRoutes(routeFile);
         tx.success();
      } catch (Exception e) {
         log.error("Exception while parsing routes file", e);
      } finally {
         tx.finish();
      }
      log.info("   ... routes file loaded in Graph");
      
      // Then trips...
      tx = template.getGraphDatabaseService().beginTx();
      log.info("Starting to parse trips file...");
      try{
         File tripFile = new File("target/test-classes/gtfs/trips.txt");
         parser.parseGTFSTrips(tripFile, routeRepository);
         tx.success();
      } catch (Exception e) {
         log.error("Exception while parsing trips file", e);
      } finally {
         tx.finish();
      }
      log.info("   ... trips file loaded in Graph");
      
      // Then stops ...
      tx = template.getGraphDatabaseService().beginTx();
      log.info("Starting to parse stops file...");
      try{
         File stopFile = new File("target/test-classes/gtfs/stops.txt");
         parser.parseGTFSStops(stopFile, routeRepository);
         tx.success();
      } catch (Exception e) {
         log.error("Exception while parsing stops file", e);
      } finally {
         tx.finish();
      }
      log.info("   ... stops file loaded in Graph");
      
      // Then stop_times ...
      tx = template.getGraphDatabaseService().beginTx();
      log.info("Starting to parse stops file...");
      try{
         File stopTimeFile = new File("target/test-classes/gtfs/stop_times.txt");
         parser.parseGTFSStopTimes(stopTimeFile, tripRepository, stopRepository);
      } catch (Exception e) {
         log.error("Exception while parsing stop_times file", e);
      } finally {
         tx.finish();
      }
      log.info("   ... stop_times file loaded in Graph");
      
      return "load/load";
   }
}
