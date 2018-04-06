package com.jlr.ddc.servicebase.controller.vehicle;

import com.jlr.ddc.servicebase.config.Constants;
import com.jlr.ddc.servicebase.domain.Vehicle;
import com.jlr.ddc.servicebase.repository.vehicle.VehicleNotFoundException;
import com.jlr.ddc.servicebase.repository.vehicle.VehicleRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/vehicles", produces = Constants.MEDIA_TYPE_APPLICATION_JSON_HAL)
public class VehicleController {
  
  private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

  VehicleRepository vehicleRepository;
  
  public VehicleController(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }
  
  /**
   * GET operation to return all items.
   * 
   * @return all items
   */
  @GetMapping
  public ResponseEntity<Resources<VehicleResource>> all() {
    
    logger.info("GET received");
    
    List<VehicleResource> allVehicles = 
        vehicleRepository.findAll()
        .stream()
        .map(VehicleResource::new)
        .collect(Collectors.toList());
    
    Resources<VehicleResource> resources = new Resources<>(allVehicles);
    
    String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
    resources.add(new Link(uriString, "ref"));
    
    return ResponseEntity.ok(resources);
  }
  
  /**
   * GET operation to return one specific item.
   * 
   * @param id identifies the item to be returned
   * @return the identified single item
   */
  @GetMapping("/{id}")
  public ResponseEntity<VehicleResource> get(@PathVariable("id") Long id) {
    
    logger.info("GET/id received with id = " + id);
    
    return vehicleRepository.findById(id)
        .map(p -> ResponseEntity.ok(new VehicleResource(p)))
        .orElseThrow(() -> new VehicleNotFoundException(id));
  }
  
  /**
   * POST operation to create an item.
   * 
   * @param vehicleFromRequest item to be created
   * @return the created item
   */
  @PostMapping
  public ResponseEntity<VehicleResource> post(@RequestBody Vehicle vehicleFromRequest) {
    
    logger.info("POST received with body = " + vehicleFromRequest.toString());
    
    Vehicle createdVehicle = vehicleRepository.save(vehicleFromRequest);
    
    URI uri = MvcUriComponentsBuilder
        .fromController(getClass())
        .path("/{id}")
        .buildAndExpand(vehicleFromRequest.getId())
        .toUri();
    
    return ResponseEntity.created(uri).body(new VehicleResource(createdVehicle));
  }
  
  /**
   * PUT operation to update an item.
   * 
   * @param id identifies the item to be updated
   * @param vehicleFromRequest the item to be updated
   * @return the updated item
   */
  @PutMapping("/{id}")
  public ResponseEntity<VehicleResource> put(
      @PathVariable("id") Long id,
      @RequestBody Vehicle vehicleFromRequest) {
    
    logger.info("PUT/id received with id = " + id);
    
    Vehicle updatedVehicle = new Vehicle(vehicleFromRequest, id);
    vehicleRepository.save(updatedVehicle);
    
    VehicleResource updatedVehicleResource = new VehicleResource(updatedVehicle);
    
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
    
    return ResponseEntity.created(uri).body(updatedVehicleResource);
  }
  
  /**
   * TODO - Implements JSON merge patch in line with RFC7386 - https://tools.ietf.org/html/rfc7386
   */
  @PatchMapping("/{id}")
  public ResponseEntity<VehicleResource> patch(
      @PathVariable("id") Long id,
      @RequestBody Vehicle vehicleFromRequest) {
    
    logger.info("PATCH/id received with id = " + id);
    
    return vehicleRepository.findById(id)
        .map(p -> {
          if (vehicleFromRequest.getName() != null) {
            p.setName(vehicleFromRequest.getName());
          }
          vehicleRepository.save(p);
          return ResponseEntity.ok().body(new VehicleResource(p));
        })
        .orElseThrow(() -> new VehicleNotFoundException(id));  
  }
  
  /**
   * DELETE operation to remove a specified item.
   * 
   * @param id identifies the item to be deleted
   * @return the deleted item
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    
    logger.info("DELETE/id received with id = " + id);
    
    return vehicleRepository.findById(id)
        .map(p -> {
          vehicleRepository.deleteById(id);
          return ResponseEntity.noContent().build();
        })
        .orElseThrow(() -> new VehicleNotFoundException(id));
  }
  
}
