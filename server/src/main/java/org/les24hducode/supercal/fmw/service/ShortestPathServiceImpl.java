package org.les24hducode.supercal.fmw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import org.les24hducode.supercal.fmw.api.PathView;
import org.les24hducode.supercal.fmw.api.RouteView;
import org.les24hducode.supercal.fmw.api.StopTimeView;
import org.les24hducode.supercal.fmw.domain.EndsEvaluator;
import org.les24hducode.supercal.fmw.domain.Route;
import org.les24hducode.supercal.fmw.domain.Section;
import org.les24hducode.supercal.fmw.domain.Stop;
import org.les24hducode.supercal.fmw.repository.RouteRepository;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;
/**
 * ShortestService default implementation.
 * @author laurent
 */
@Service("shortestPathService")
public class ShortestPathServiceImpl implements ShortestPathService {

   /** A commons logger for diagnostic messages. */
   private static Log log = LogFactory.getLog(ShortestPathServiceImpl.class);

   @Autowired
   private RouteRepository routeRepository;
   
   @Autowired
   private StopRepository stopRepository;
   
   @Autowired
   private Neo4jTemplate template;
   
   @Override
   public List<PathView> getShortestPath(Double latitude, Double longitude, Double destinationLatitude, Double destinationLongitude) {
      // Display some fancy trace messages.
      log.info("Being at (" + latitude + ", " + longitude + ")");
      log.info("  Computing shortest path for (" + destinationLatitude + ", " + destinationLongitude + ")");
      
      /* Ca marche, on garde au cas où ...
      Iterable<Section> sections = template.findAll(Section.class);
      int sectionNb = 0;
      for (Section section : sections){
         sectionNb++;
      }
      log.info("Found " + sectionNb + " sections");
       
      List<Stop> startStops = new ArrayList<Stop>();
      Stop stop = stopRepository.getStopById("\"2005\"");
      startStops.add(stop);
      
      Stop stop1 = stopRepository.getStopById("\"1052\"");
      Stop stop2 = stopRepository.getStopById("\"1080\"");
      Stop stop3 = stopRepository.getStopById("\"1168\"");
      Stop stop4 = stopRepository.getStopById("\"1315\"");
      Stop stop5 = stopRepository.getStopById("\"1550\"");
      Stop stop6 = stopRepository.getStopById("\"1551\"");
            
      List<Node> endNodes = new ArrayList<Node>();
      endNodes.add(stop1.getPersistentState());
      endNodes.add(stop2.getPersistentState());
      endNodes.add(stop3.getPersistentState());
      endNodes.add(stop4.getPersistentState());
      endNodes.add(stop5.getPersistentState());
      endNodes.add(stop6.getPersistentState());
      
      TraversalDescription td = Traversal.description()
            .breadthFirst().uniqueness(Uniqueness.NODE_PATH)
            .relationships(DynamicRelationshipType.withName("SECTION"))
            .evaluator(Evaluators.all())
            .evaluator(new EndsEvaluator(endNodes));
    
      Traverser t1 = td.traverse(stop.getPersistentState());
      */
      
      Iterable<Stop> startStops = stopRepository.findWithinDistance("stopLocation", latitude, longitude, 0.2);
      Iterable<Stop> endStops = stopRepository.findWithinDistance("stopLocation", destinationLatitude, destinationLongitude, 0.2);
      
      List<Node> endNodes = new ArrayList<Node>();
      for (Stop stop : endStops){
         log.info("-> found end stop : " + stop.getWkt()  + " - " + stop.getId());
         endNodes.add(stop.getPersistentState());
      }
      
      // Prepare result container.
      List<PathView> results = new ArrayList<PathView>();
      
      for (Stop start : startStops){
         log.info("-> found start stop : " + start.getWkt()  + " - " + start.getId());
         
         // Search for paths from each start point, traversing graph.
         TraversalDescription traversalDescription = Traversal.description()
             .breadthFirst().uniqueness(Uniqueness.NODE_PATH)
             .relationships(DynamicRelationshipType.withName("SECTION"))
             .evaluator(Evaluators.all())
             .evaluator(new EndsEvaluator(endNodes));
       
         Traverser t = traversalDescription.traverse(start.getPersistentState());
         
         for (Path position : t) {
            // Compose a PathView for each proposed path.
            log.debug("Path from start node to current position is " + position);
            
            PathView pathView = new PathView();
            Map<String, RouteView> routesMap = new HashMap<String, RouteView>();
            
            // We should keep last route id in case of terminal node.
            String lastRouteId = null;
            
            // Iterate same time on nodes and relations.
            Iterator<Relationship> relIterator = position.relationships().iterator();
            for (Node node : position.nodes()){
              log.info("Found node: " + node + " - " + node.getId() + " - " + node.getClass());
              
              String routeId = null;
              if (relIterator.hasNext()){
                 Relationship relation = relIterator.next();
                 Section section = template.findOne(relation.getId(), Section.class);
                 routeId = section.getRouteId();
                 lastRouteId = routeId;
              } else {
                 routeId = lastRouteId;
              }
              
              RouteView rv = routesMap.get(routeId);
              if (rv == null){
                 // Retrieve route and create partial representation.
                 Route route = routeRepository.getRouteById(routeId);
                 rv = new RouteView();
                 rv.setShortName(route.getShortName());
                 rv.setLongName(route.getLongName());
                 rv.setDescription(route.getDescription());
                 routesMap.put(routeId, rv);
              }
              
              // Retrieve stop and build an aggregate view with corresponding StopTime. 
              Stop stopTemp = template.findOne(node.getId(), Stop.class);
              StopTimeView stv = new StopTimeView();
              stv.setLatitude(stopTemp.getLatitude());
              stv.setLongitude(stopTemp.getLongitude());
              stv.setName(stopTemp.getName());
              
              rv.getStopTimes().add(stv);
            }
            
            pathView.setRoutes(new ArrayList<RouteView>(routesMap.values()));
            results.add(pathView);
         }
      }
      
      /* Les gros bouchons qui fonctionnent bien ...
      List<PathView> results = new ArrayList<PathView>();
      
      PathView pv1 = new PathView();
      pv1.setDepartureLatitude(47.56);
      pv1.setDepartureLongitude(-1.66);
      
      StopTimeView stv1 = new StopTimeView();
      stv1.setName("Mission St. & Silver Ave.");
      stv1.setLatitude(47.56);
      stv1.setLongitude(-1.66);
      stv1.setArrivalTime("062012");
      
      StopTimeView stv2 = new StopTimeView();
      stv2.setName("Mission St. & Cortland Ave.");
      stv2.setLatitude(47.56);
      stv2.setLongitude(-1.6610);
      stv2.setArrivalTime("062012");
      
      RouteView route = new RouteView();
      route.setShortName("17");
      route.setLongName("Mission");
      route.setDescription("The A route travels from lower Mission to Downtown.");
      
      route.getStopTimes().add(stv1);
      route.getStopTimes().add(stv2);
      
      pv1.getRoutes().add(route);
      results.add(pv1);
      */
      
      return results;
   }
}
