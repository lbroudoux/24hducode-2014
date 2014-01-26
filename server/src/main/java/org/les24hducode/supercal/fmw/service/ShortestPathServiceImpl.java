package org.les24hducode.supercal.fmw.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.les24hducode.supercal.fmw.api.PathView;
import org.les24hducode.supercal.fmw.api.RouteView;
import org.les24hducode.supercal.fmw.api.StopTimeView;
import org.les24hducode.supercal.fmw.repository.StopRepository;
/**
 * ShortestService default implementation.
 * @author laurent
 */
@Service("shortestPathService")
public class ShortestPathServiceImpl implements ShortestPathService {

   /** A commons logger for diagnostic messages. */
   private static Log log = LogFactory.getLog(ShortestPathServiceImpl.class);

   @Autowired
   private StopRepository stopRepository;
   
   @Override
   public List<PathView> getShortestPath(Double latitude, Double longitude, Double destinationLatitude, Double destinationLongitude) {
      // Display some fancy trace messages.
      log.info("Being at (" + latitude + "," + longitude + ")");
      log.info("  Computing shortest path for (" + destinationLatitude + ", " + destinationLongitude + ")");
      
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
      
      return results;
   }
}
