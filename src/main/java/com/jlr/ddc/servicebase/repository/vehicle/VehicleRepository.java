package com.jlr.ddc.servicebase.repository.vehicle;

import com.jlr.ddc.servicebase.domain.Vehicle;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
  
  public Optional<Vehicle> findById(Long id);
  
  @Transactional
  public void deleteById(Long id);
}
