package com.tracking.tracking_app.Controllers;

import com.tracking.tracking_app.DTOs.MarkerRequestDTO;
import com.tracking.tracking_app.Services.MarkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping(path = "/api")
public class MarkerController {
    private final MarkerService markerService;

    public MarkerController(MarkerService markerService){
        this.markerService = markerService;
    }

    @PostMapping("/save-marker")
    public ResponseEntity<String> saveAllMarker(@RequestHeader("Authorization") String token, @RequestBody MarkerRequestDTO markerRequest) {
        markerService.saveMarker(markerRequest);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully saved the marker");
    }

    @PostMapping("/fetch-all-markers")
    public ResponseEntity<?> fetchAllMarkers(){
        List<Map<String, Serializable>> coordinates = markerService.fetchMarkers();

        return !coordinates.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(coordinates) : ResponseEntity.status(HttpStatus.NO_CONTENT).body("No results found");
    }

    @DeleteMapping("/delete-marker")
    public ResponseEntity<String> removerMarker(@RequestBody MarkerRequestDTO markerRequestDTO) {
        markerService.removeMarker(markerRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully removed the marker");
    }
}
