package com.jlr.ddc.servicebase.repository.vehicle;

public class VehicleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 621915313663894171L;

  private final Long id;
  
  public VehicleNotFoundException(final Long id) {
    super("Vehicle could not be found with id: " + id);
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
