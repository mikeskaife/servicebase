package com.jlr.ddc.servicebase.controller.vehicle;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlr.ddc.servicebase.config.Constants;
import com.jlr.ddc.servicebase.domain.Vehicle;
import com.jlr.ddc.servicebase.repository.vehicle.VehicleNotFoundException;
import com.jlr.ddc.servicebase.repository.vehicle.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class VehicleControllerTest {

  private MockMvc mockMvc;
  
  private ObjectMapper mapper;
  
  @Mock
  private VehicleRepository mockVehicleRepository;
  
  @InjectMocks
  VehicleController target;
  
  Vehicle testVehicle;
  
  /**
   * Setup method which configures MockMvc before use by each test.
   */
  @Before
  public void setup() {
    
    MockitoAnnotations.initMocks(this);
    
    mockMvc = MockMvcBuilders
      .standaloneSetup(target)
      .setControllerAdvice(new VehicleControllerAdvice())
      .build();
    
    mapper = new ObjectMapper();
  }
  
  @Test
  public void getAllVehiclesReturnsCorrectResponse() throws Exception {
    
    Vehicle testVehicle1 = new Vehicle();
    testVehicle1.setId(1L);
    testVehicle1.setName("Range Rover Velar");
    
    Vehicle testVehicle2 = new Vehicle();
    testVehicle2.setId(2L);
    testVehicle2.setName("Discovery Sport");
    
    List<Vehicle> vehicleList = new ArrayList<>();
    vehicleList.add(testVehicle1);
    vehicleList.add(testVehicle2);
    
    when(mockVehicleRepository.findAll()).thenReturn(vehicleList);
    
    mockMvc.perform(get("/api/vehicles"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(Constants.MEDIA_TYPE_APPLICATION_JSON_HAL))
      .andExpect(jsonPath("content[0].vehicle.id", is(1)))
      .andExpect(jsonPath("content[0].vehicle.name", is("Range Rover Velar")))
      .andExpect(jsonPath("content[0].links[0].href", is("http://localhost/api/vehicles")))
      .andExpect(jsonPath("content[0].links[1].href", is("http://localhost/api/vehicles/1")))
      .andExpect(jsonPath("content[1].vehicle.id", is(2)))
      .andExpect(jsonPath("content[1].vehicle.name", is("Discovery Sport")))
      .andExpect(jsonPath("content[1].links[0].href", is("http://localhost/api/vehicles")))
      .andExpect(jsonPath("content[1].links[1].href", is("http://localhost/api/vehicles/2")));
    
    verify(mockVehicleRepository, times(1)).findAll();
    verifyNoMoreInteractions(mockVehicleRepository);
  }
  
  @Test
  public void getSingleVehicleReturnsCorrectResponse() throws Exception {
    
    Vehicle testVehicle = new Vehicle();
    testVehicle.setId(1L);
    testVehicle.setName("Range Rover Velar");
    
    when(mockVehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
    
    mockMvc.perform(get("/api/vehicles/1"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(Constants.MEDIA_TYPE_APPLICATION_JSON_HAL))
      .andExpect(jsonPath("vehicle.id", is(1)))
      .andExpect(jsonPath("vehicle.name", is("Range Rover Velar")))
      .andExpect(jsonPath("links[0].href", is("http://localhost/api/vehicles")))
      .andExpect(jsonPath("links[1].href", is("http://localhost/api/vehicles/1")));
    
    verify(mockVehicleRepository, times(1)).findById(1L);
    verifyNoMoreInteractions(mockVehicleRepository);
  }

  @Test
  public void getVehicleThatDoesNotExistReturnsError() throws Exception {
    
    final VehicleNotFoundException exception = new VehicleNotFoundException(1L);
    
    when(mockVehicleRepository.findById(1L)).thenReturn(Optional.empty());
    
    mockMvc.perform(get("/api/vehicles/1"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$[0].logref", is(String.valueOf(1L))))
      .andExpect(jsonPath("$[0].message", is(exception.getMessage())))
      .andExpect(jsonPath("$[0].links", is(new ArrayList<String>())));
  }
  
  @Test
  public void createVehicleReturnsCorrectResponse() throws Exception {
    
    Vehicle testVehicle = new Vehicle();
    testVehicle.setId(1L);
    testVehicle.setName("Range Rover Velar");
    
    when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
    
    mockMvc.perform(post("/api/vehicles")
      .content(mapper.writeValueAsBytes(testVehicle))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("vehicle.id", is(1)))
      .andExpect(jsonPath("vehicle.name", is("Range Rover Velar")))
      .andExpect(jsonPath("links[0].href", is("http://localhost/api/vehicles")))
      .andExpect(jsonPath("links[1].href", is("http://localhost/api/vehicles/1")));
    
    verify(mockVehicleRepository, times(1)).save(any(Vehicle.class));
    verifyNoMoreInteractions(mockVehicleRepository);
  }
}
