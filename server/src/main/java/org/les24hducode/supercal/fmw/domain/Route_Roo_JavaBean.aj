// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.les24hducode.supercal.fmw.domain;

import java.util.Set;
import org.les24hducode.supercal.fmw.domain.Route;
import org.les24hducode.supercal.fmw.domain.Trip;

privileged aspect Route_Roo_JavaBean {
    
    public Long Route.getNodeId() {
        return this.nodeId;
    }
    
    public void Route.setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    
    public String Route.getId() {
        return this.id;
    }
    
    public void Route.setId(String id) {
        this.id = id;
    }
    
    public String Route.getShortName() {
        return this.shortName;
    }
    
    public void Route.setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String Route.getLongName() {
        return this.longName;
    }
    
    public void Route.setLongName(String longName) {
        this.longName = longName;
    }
    
    public String Route.getDescription() {
        return this.description;
    }
    
    public void Route.setDescription(String description) {
        this.description = description;
    }
    
    public String Route.getRouteType() {
        return this.routeType;
    }
    
    public void Route.setRouteType(String routeType) {
        this.routeType = routeType;
    }
    
    public Set<Trip> Route.getTrips() {
        return this.trips;
    }
    
    public void Route.setTrips(Set<Trip> trips) {
        this.trips = trips;
    }
    
}
