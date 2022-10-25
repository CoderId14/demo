package com.example.demo.api.f1;


import com.example.demo.Service.f1.impl.RacerService;
import com.example.demo.dto.f1.RacerDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/racer")
@AllArgsConstructor
@Slf4j
public class RacerController {
    private final RacerService racerService;


    @GetMapping
    ResponseEntity<?> getAllRacers(){
        return ResponseEntity.ok().body(
                racerService.getAllRacers()
        );
    }
    @PostMapping
    ResponseEntity<?> addRacer(@RequestBody RacerDto racerDto){
        return ResponseEntity.ok().body(
                racerService.addRacer(racerDto)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateRacer(@RequestBody RacerDto racerDto, @PathVariable Long id){
        log.info("update racer id= " + id);
        return ResponseEntity.ok().body(
                racerService.updateRacer(racerDto, id)
        );
    }
    @PutMapping()
    ResponseEntity<?> updateRacers(@RequestBody RacerDto[] racerDtos){
        return ResponseEntity.ok().body(
                racerService.updateRacers(racerDtos)
        );
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> updateRacer(@PathVariable Long id){
        return ResponseEntity.ok().body(
                racerService.deleteRacer(id)
        );
    }
}
