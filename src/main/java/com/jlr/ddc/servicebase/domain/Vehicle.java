package com.jlr.ddc.servicebase.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle {

  public Vehicle() {
    
  }
  
  public Vehicle(String name) {
    this.name = name;
  }
  
  public Vehicle(Long id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public Vehicle(Vehicle vehicle) {
    this.id = vehicle.getId();
    this.name = vehicle.getName();
  }

  public Vehicle(Vehicle vehicle, Long id) {
    this.id = id;
    this.name = vehicle.getName();
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "name")
  private String name;

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
}
