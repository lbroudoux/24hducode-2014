package org.les24hducode.supercal.fmw.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.les24hducode.supercal.fmw.domain.Route;
import org.les24hducode.supercal.fmw.domain.Section;
import org.les24hducode.supercal.fmw.domain.Stop;
import org.les24hducode.supercal.fmw.domain.Trip;
import org.les24hducode.supercal.fmw.repository.RouteRepository;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.les24hducode.supercal.fmw.repository.TripRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
/**
 * A parser for GTFS files that fills a Neo4j datastore.
 * @author laurent
 */
public class GTFSParser {

   /** A commons logger for diagnostic messages. */
   private static Log log = LogFactory.getLog(GTFSParser.class);
   
   private Neo4jTemplate template;
   
   private Map<String, Route> routesMap = new HashMap<String, Route>();
   
   private Map<String, Trip> tripsMap = new HashMap<String, Trip>();
   
   private Map<String, Stop> stopsMap = new HashMap<String, Stop>();
   
   
   public GTFSParser(Neo4jTemplate template){
      this.template = template;
   }
   
   public void parseGTFSRoutes(File routesFile) throws IOException{
      // Browse file lines.
      BufferedReader reader = new BufferedReader(new FileReader(routesFile));
      String line = reader.readLine();
      while (line != null){
         // Evict header line.
         if (!line.startsWith("route_id")){
            //route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color
            String[] props = line.split(",");
            // Fill domain object from line elements.
            Route route = new Route();
            route.setId(props[0]);
            route.setShortName(props[1]);
            route.setLongName(props[2]);
            route.setDescription(props[3]);
            route.setRouteType(props[4]);
            
            template.save(route);
            routesMap.put(route.getId(), route);
         }
         line = reader.readLine();
      }
      reader.close();
   }
   
   public void parseGTFSTrips(File tripsFile, RouteRepository repository) throws IOException{
      // Browse file lines.
      BufferedReader reader = new BufferedReader(new FileReader(tripsFile));
      String line = reader.readLine();
      int i = 1;
      while (line != null){
         if (i % 50 == 0){
            log.info("   ... done " + i + " trips ...");
         }
         // Evict header line.
         if (!line.startsWith("trip_id")){
            // trip_id,service_id,route_id,trip_headsign,direction_id,block_id
            String[] props = line.split(",");
            // Fill domain object from line elements.
            Trip trip = new Trip();
            trip.setId(props[0]);
            trip.setHeadSign(props[3]);
            
            //Route route = repository.getRouteById(props[2]);
            Route route = routesMap.get(props[2]);
            trip.setRoute(route);
            
            template.save(trip);
            tripsMap.put(trip.getId(), trip);
         }
         i++;
         line = reader.readLine();
      }
      reader.close();
   }
   
   public void parseGTFSStops(File stopsFile, RouteRepository repository) throws IOException{
      // Browse file lines.
      BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
      String line = reader.readLine();
      int i = 1;
      while (line != null){
         if (i % 50 == 0){
            log.info("   ... done " + i + " stops ...");
         }
         // Evict header line.
         if (!line.startsWith("stop_id")){
            // stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url,location_type,parent_station,stop_timezone,wheelchair_boarding
            String[] props = line.split(",");
            // Fill domain object from line elements.
            Stop stop = new Stop();
            stop.setId(props[0]);
            stop.setName(props[2]);
            stop.setDescription(props[3]);
            
            String latitude = sanitize(props[4]);
            stop.setLatitude(Double.valueOf(latitude));
            
            String longitude = sanitize(props[5]);
            stop.setLongitude(Double.valueOf(longitude));
            stop.setUrl(props[7]);
            
            stop.setLocation(Double.valueOf(latitude), Double.valueOf(longitude));
            
            template.save(stop);
            stopsMap.put(stop.getId(), stop);
         }
         i++;
         line = reader.readLine();
      }
      reader.close();
   }
   
   public void parseGTFSStopTimes(File stopTimesFile, TripRepository tripRepository, StopRepository stopRepository) throws IOException{
      // Key is trip id, values are corresponding stop times.
      Map<String, List<StopTime>> tripToStopTimes = new HashMap<String, List<StopTime>>();
      
      // Browse file lines.
      BufferedReader reader = new BufferedReader(new FileReader(stopTimesFile));
      String line = reader.readLine();
      int i = 1;
      while (line != null){
         if (i % 50 == 0){
            log.info("   ... done " + i + " stopTimes ...");
         }
         // Evict header line.
         if (!line.startsWith("trip_id")){
            // trip_id,stop_id,stop_sequence,arrival_time,departure_time,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled
            String[] props = line.split(",");
            // Fill domain object from line elements.
            StopTime time = new StopTime(props[0], props[1]);
            time.stopHeadsign = props[5];
            
            // Parse complex events.
            String sequence = sanitize(props[2]);
            time.sequence = Integer.parseInt(sequence);
            String arrivalTime = sanitize(props[3]);
            time.arrivalTime = parseTime(arrivalTime);
            String departureTime = sanitize(props[4]);
            time.departureTime = parseTime(departureTime);
            
            List<StopTime> times = tripToStopTimes.get(time.tripId);
            if (times == null){
               times = new ArrayList<StopTime>();
               tripToStopTimes.put(time.tripId, times);
            }
            times.add(time);
         }
         i++;
         line = reader.readLine();
      }
      reader.close();
      
      //
      List<Route> alreadyProcessed = new ArrayList<Route>();
      
      for (String tripId : tripToStopTimes.keySet()){
         List<StopTime> times = tripToStopTimes.get(tripId);
         Collections.sort(times);
         
         //
         //Trip trip = tripRepository.getTripById(tripId);
         Trip trip = tripsMap.get(tripId);
         Route route = trip.getRoute();
         
         if (!alreadyProcessed.contains(route)){
            System.err.println("Processing stops for route " + route.getId());
            for (int j=0; j<times.size()-1; j++){
               //Stop startStop = stopRepository.getStopById(times.get(j).stopId); 
               //Stop endStop = stopRepository.getStopById(times.get(j+1).stopId);
               
               Stop startStop = stopsMap.get(times.get(j).stopId); 
               Stop endStop = stopsMap.get(times.get(j+1).stopId);
               
               /*
               startStop.setRouteId(route.getNodeId());
               endStop.setRouteId(route.getNodeId());
               startStop.getRouteStops().add(endStop);
               */
               
               /*
               Section section = template.createRelationshipBetween(startStop, endStop, Section.class, "SECTION", true);
               template.save(startStop);
               */
               
               /*
               Section section = startStop.leadTo(endStop, route.getId());
               template.save(startStop);
               */
               Section section = new Section(startStop, endStop, route.getId());
               template.save(section);
            }
            alreadyProcessed.add(route);
         }
      }
   }
   
   private String sanitize(String value){
      if (value.startsWith("\"")){
         value = value.substring(1);
      }
      if (value.endsWith("\"")){
         value = value.substring(0, value.length() - 1);
      }
      return value;
   }
   
   private int parseTime(String time){
      String[] elem = time.split(":");
      return (Integer.parseInt(elem[0]) * 60) + Integer.parseInt(elem[1]);
   }
   
   // Inner classes ------------------------------------------------------------
   
   private class StopTime implements Comparable{
      private String tripId;
      private String stopId;
      private int sequence;
      private int arrivalTime;
      private int departureTime;
      private String stopHeadsign;
      
      public StopTime(String tripId, String stopId){
         this.tripId = tripId;
         this.stopId = stopId;
      }

      @Override
      public int compareTo(Object obj){
         if (obj instanceof StopTime){
            StopTime other = (StopTime)obj;
            if (other.tripId.equals(this.tripId)){
               // 3 - 4 = -1   ==>  3 is less than 4
               return (this.sequence - other.sequence);
            }
         }
         return 0;
      }
   }
}
