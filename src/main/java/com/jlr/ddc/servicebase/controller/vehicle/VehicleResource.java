package com.jlr.ddc.servicebase.controller.vehicle;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.jlr.ddc.servicebase.domain.Vehicle;

import org.springframework.hateoas.ResourceSupport;

public class VehicleResource extends ResourceSupport {

  private final Vehicle vehicle;
  
  /**
   * Adds HAL/HATEOAS links to a Vehicle resource.
   * 
   * @param vehicle the Vehicle object
   */
  public VehicleResource(Vehicle vehicle) {
    
    this.vehicle = vehicle;
    final long id = vehicle.getId();
    
    add(linkTo(VehicleController.class).withRel("vehicles"));
    add(linkTo(methodOn(VehicleController.class).get(id)).withSelfRel());
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

}
