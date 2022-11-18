package com.example.demo.api.f1;


import com.example.demo.Service.f1.impl.GrandPrixService;
import com.example.demo.Service.f1.impl.RacerService;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.RacerDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grand-prix")
@AllArgsConstructor
@Slf4j
public class GrandPrixController {
        private final GrandPrixService grandPrixService;


        @GetMapping
        ResponseEntity<?> getAllGrandPrix(){
                return ResponseEntity.ok().body(
                        grandPrixService.getAllGrandPrixes()
                );
        }
        @GetMapping("/season")
        ResponseEntity<?> getAllGrandPrixesBySeason(@RequestParam Long seasonId){
                return ResponseEntity.ok().body(
                        grandPrixService.getAllGrandPrixesBySeason(seasonId)
                );
        }
        @PostMapping
        ResponseEntity<?> addGrandPrix(@RequestBody GrandPrixDto grandPrixDto){
                return ResponseEntity.ok().body(
                        grandPrixService.addGrandPrix(grandPrixDto)
                );
        }

        @PutMapping("/{id}")
        ResponseEntity<?> updateGrandPrix(@RequestBody GrandPrixDto grandPrixDto, @PathVariable Long id){
                log.info("update grandPrix id= " + id);
                return ResponseEntity.ok().body(
                        grandPrixService.updateGrandPrix(grandPrixDto, id)
                );
        }

        @DeleteMapping("/{id}")
        ResponseEntity<?> deleteGrandPrix(@PathVariable Long id){
                return ResponseEntity.ok().body(
                        grandPrixService.deleteGrandPrix(id)
                );
        }
}
