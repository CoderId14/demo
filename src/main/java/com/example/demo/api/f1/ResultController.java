package com.example.demo.api.f1;


import com.example.demo.Service.f1.impl.ResultService;
import com.example.demo.dto.f1.RacerGrandPrixDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/result")
@AllArgsConstructor
@Slf4j
public class ResultController {

    private final ResultService resultService;


    @GetMapping("/racer-ranking")
    public ResponseEntity<?> getRacerRanking(@RequestParam(required = false) Long seasonId){
        if(seasonId != null){
            return ResponseEntity.ok().body(
                    resultService.getRankingRacersBySeason(seasonId)
            );
        }
        return ResponseEntity.ok().body(
                resultService.getRankingRacers()
        );
    }
    @GetMapping("/racer")
    public ResponseEntity<?> getRacerResultDetail(@RequestParam Long racerId, @RequestParam Long seasonId){
        return ResponseEntity.ok().body(
                resultService.getRacerResultDetail(racerId, seasonId)
        );
    }
    @PostMapping("/racer")
    public ResponseEntity<?> addRacerToResult(@RequestBody RacerGrandPrixDto racerGrandPrixDto){
        return ResponseEntity.ok().body(
                resultService.addRacerToResult(racerGrandPrixDto.getSeasonId()
                        , racerGrandPrixDto.getGrandPrixId(),
                        racerGrandPrixDto.getRacerOfTeamId())
        );
    }
    @PostMapping("/racer/delete")
    public ResponseEntity<?> deleteResult(@RequestBody RacerGrandPrixDto racerGrandPrixDto){
        return ResponseEntity.ok().body(
                resultService.deleteResult(racerGrandPrixDto.getSeasonId()
                        , racerGrandPrixDto.getGrandPrixId(),
                        racerGrandPrixDto.getRacerOfTeamId())
        );
    }
}
