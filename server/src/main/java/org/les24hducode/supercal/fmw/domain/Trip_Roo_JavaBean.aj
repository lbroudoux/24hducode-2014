// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.les24hducode.supercal.fmw.domain;

import org.les24hducode.supercal.fmw.domain.Route;
import org.les24hducode.supercal.fmw.domain.Trip;

privileged aspect Trip_Roo_JavaBean {
    
    public Long Trip.getNodeId() {
        return this.nodeId;
    }
    
    public void Trip.setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    
    public String Trip.getId() {
        return this.id;
    }
    
    public void Trip.setId(String id) {
        this.id = id;
    }
    
    public String Trip.getHeadSign() {
        return this.headSign;
    }
    
    public void Trip.setHeadSign(String headSign) {
        this.headSign = headSign;
    }
    
    public Route Trip.getRoute() {
        return this.route;
    }
    
    public void Trip.setRoute(Route route) {
        this.route = route;
    }
    
}
