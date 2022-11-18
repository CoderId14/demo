package com.example.demo.api.f1;


import com.example.demo.Service.f1.impl.GrandPrixService;
import com.example.demo.Service.f1.impl.RaceTeamService;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.RaceTeamDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/race-team")
@AllArgsConstructor
@Slf4j
public class RaceTeamController {
        private final RaceTeamService raceTeamService;


        @GetMapping
        ResponseEntity<?> getAllRaceTeams(){
                return ResponseEntity.ok().body(
                        raceTeamService.getAllRaceTeams()
                );
        }
        @GetMapping("/season")
        ResponseEntity<?> getAllRaceBySeason(@RequestParam Long seasonId){
                return ResponseEntity.ok().body(
                        raceTeamService.getRaceTeamBySeason(seasonId)
                );
        }
        @PostMapping
        ResponseEntity<?> addRaceTeam(@RequestBody RaceTeamDto raceTeamDto){
                return ResponseEntity.ok().body(
                        raceTeamService.addRaceTeam(raceTeamDto)
                );
        }
        @PostMapping("/{seasonId}")
        ResponseEntity<?> addRaceTeamInSeason(@RequestBody RaceTeamDto raceTeamDto, @PathVariable Long seasonId){
                return ResponseEntity.ok().body(
                        raceTeamService.addRaceTeamInSeason(seasonId, raceTeamDto)
                );
        }

        @PutMapping("/{id}")
        ResponseEntity<?> updateRaceTeam(@PathVariable Long id, @RequestBody RaceTeamDto raceTeamDto){
                log.info("update raceteam id= " + id);
                return ResponseEntity.ok().body(
                        raceTeamService.updateRaceTeam(id, raceTeamDto)
                );
        }


        @DeleteMapping("/{id}")
        ResponseEntity<?> deleteRaceTeam(@PathVariable Long id){
                return ResponseEntity.ok().body(
                        raceTeamService.deleteRaceTeam(id)
                );
        }
        @DeleteMapping("/{raceTeamId}")
        ResponseEntity<?> deleteRaceTeamInSeason(@PathVariable Long raceTeamId, @RequestBody RaceTeamDto raceTeamDto){
                return ResponseEntity.ok().body(
                        raceTeamService.deleteRaceTeamInSeason(raceTeamId, raceTeamDto)
                );
        }
}
