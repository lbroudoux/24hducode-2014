package org.les24hducode.supercal.fmw.service;

import java.util.List;

import org.les24hducode.supercal.fmw.api.PathView;
/**
 * Service interface for retrieving path proposals.
 * @author laurent
 */
public interface ShortestPathService {

   public List<PathView> getShortestPath(Double latitude, Double longitude, 
         Double destinationLatitude, Double destinationLongitude);
}
