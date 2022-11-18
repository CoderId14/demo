package com.example.demo.api.f1;

import com.example.demo.Service.f1.impl.RacerOfTeamService;
import com.example.demo.dto.f1.RacerOfTeamDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/racerOfTeam")
@AllArgsConstructor
@Slf4j
public class RacerOfTeamController {
    private final RacerOfTeamService racerOfTeamService;

    @GetMapping
    ResponseEntity<?> getAllRacerOfTeam() {
        return ResponseEntity.ok().body(
                racerOfTeamService.getAllRacerOfTeam()
        );
    }

    @GetMapping("/test1")
    ResponseEntity<?> getRacerNotInGrandPrix(@RequestParam Long seasonId
            , @RequestParam Long grandPrixId
            , @RequestParam Long raceTeamId) {
        return ResponseEntity.ok().body(
                racerOfTeamService.getRacerNotInGrandPrix(seasonId,grandPrixId,raceTeamId)
        );
    }

    @GetMapping("/test2")
    ResponseEntity<?> getRacerInGrandPrix(@RequestParam Long seasonId
            , @RequestParam Long grandPrixId
            , @RequestParam Long raceTeamId) {
        return ResponseEntity.ok().body(
                racerOfTeamService.getRacerInGrandPrix(seasonId,grandPrixId,raceTeamId)
        );
    }

    @PostMapping
    ResponseEntity<?> addRacerToTeam(@RequestBody RacerOfTeamDto racerOfTeamDto) {
        return ResponseEntity.ok().body(
                racerOfTeamService.addRacertoTeam(racerOfTeamDto)
        );
    }
}
