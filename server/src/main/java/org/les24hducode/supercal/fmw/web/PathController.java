package org.les24hducode.supercal.fmw.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.les24hducode.supercal.fmw.api.PathView;
import org.les24hducode.supercal.fmw.service.ShortestPathService;
/**
 * The web controller for /path resources.
 * @author laurent
 */
@Controller
@RequestMapping("/path")
public class PathController {

   /** A commons logger for diagnostic messages. */
   private static Log log = LogFactory.getLog(PathController.class);
   
   @Autowired
   private ShortestPathService pathService;
   
   @RequestMapping(value = "/find", method = RequestMethod.GET)
   public String find(
         @RequestParam("lat") Double latitude, 
         @RequestParam("lng") Double longitude,
         @RequestParam("destLat") Double destinationLat, 
         @RequestParam("destLng") Double destinationLng,
         ModelMap modelMap){
      
      log.info("In find()");
      
      // Call service and simply update model map.
      List<PathView> paths = pathService.getShortestPath(latitude, longitude, destinationLat, destinationLng);
      modelMap.addAttribute("paths", paths);
      
      return "path/find";
   }
}
