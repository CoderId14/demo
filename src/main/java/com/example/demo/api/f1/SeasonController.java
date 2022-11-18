package com.example.demo.api.f1;


import com.example.demo.Service.f1.impl.SeasonService;
import com.example.demo.dto.f1.SeasonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/season")
@AllArgsConstructor
@Slf4j
public class SeasonController {
        private final SeasonService seasonService;


        @GetMapping
        ResponseEntity<?> getAllSeason(){
                return ResponseEntity.ok().body(
                        seasonService.getAllSeasons()
                );
        }
        @PostMapping
        ResponseEntity<?> addSeason(@RequestBody SeasonDto seasonDto){
                return ResponseEntity.ok().body(
                        seasonService.addSeason(seasonDto)
                );
        }

        @PutMapping("/{id}")
        ResponseEntity<?> updateSeason(@RequestBody SeasonDto seasonDto, @PathVariable Long id){
                log.info("update Season id= " + id);
                return ResponseEntity.ok().body(
                        seasonService.updateSeason(id, seasonDto)
                );
        }
        @PutMapping("/{seasonId}/race-team/{raceTeamId}")
        ResponseEntity<?> updateRaceTeam(@PathVariable Long seasonId,
                                         @PathVariable Long raceTeamId)
        {
                log.info("update raceteam id= " + raceTeamId);
                return ResponseEntity.ok().body(
                        seasonService.updateRaceTeamInSeason(raceTeamId, seasonId)
                );
        }

        @DeleteMapping("/{id}")
        ResponseEntity<?> deleteSeason(@PathVariable Long id){
                return ResponseEntity.ok().body(
                        seasonService.deleteSeason(id)
                );
        }
}
