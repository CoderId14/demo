package com.example.demo.Service.f1.impl;


import com.example.demo.Repository.f1.*;
import com.example.demo.Service.f1.IResultService;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.RacerOfTeamDto;
import com.example.demo.dto.f1.RacerResultDetail;
import com.example.demo.dto.f1.ResultRacerDto;
import com.example.demo.entity.f1.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class ResultService implements IResultService {

    private final IGrandPrixRepo grandPrixRepo;
    private final IRacerOfRaceTeamRepo racerTeamRepo;
    private final IRaceTeamRepo raceTeamRepo;
    private final ISeasonRepo seasonRepo;
    private final IResultRepo resultRepo;
    public List<ResultRacerDto> getRankingRacers(){
        List<ResultRacerDto> results = resultRepo.findRacerRanking();
        return results;
    }
    public List<ResultRacerDto> getRankingRacersBySeason(Long seasonId){
        List<ResultRacerDto> results = resultRepo.findRacerRanking(seasonId);
        return results;
    }
    public List<RacerResultDetail> getRacerResultDetail(Long racerId, Long seasonId){
        List<RacerResultDetail> results = resultRepo.findRacerResultDetail(racerId,seasonId);
        return results;
    }

    public boolean addRacerToResult(Long seasonId, Long grandPrixId, Long racerOfTeamId){
        Season season = seasonRepo.findById(seasonId).orElseThrow(
                ()-> new ResourceNotFoundException("Season", "id", seasonId)
        );
        GrandPrix grandPrix = grandPrixRepo.findById(grandPrixId).orElseThrow(
                ()-> new ResourceNotFoundException("grandPrix", "id", grandPrixId)
        );
        RacerOfRaceTeam racerOfRaceTeam = racerTeamRepo.findById(racerOfTeamId).orElseThrow(
                ()-> new ResourceNotFoundException("racerOfTeam", "id", racerOfTeamId)
        );
        Result result = Result.builder()
                .grandPrix(grandPrix)
                .racerOfRaceTeam(racerOfRaceTeam)
                .status(0)
                .startTime(grandPrix.getTime())
                .build();
        resultRepo.save(result);
        return true;
    }
    public boolean deleteResult(Long seasonId, Long grandPrixId, Long racerOfTeamId){
        Season season = seasonRepo.findById(seasonId).orElseThrow(
                ()-> new ResourceNotFoundException("Season", "id", seasonId)
        );
        GrandPrix grandPrix = grandPrixRepo.findById(grandPrixId).orElseThrow(
                ()-> new ResourceNotFoundException("grandPrix", "id", grandPrixId)
        );
        RacerOfRaceTeam racerOfRaceTeam = racerTeamRepo.findById(racerOfTeamId).orElseThrow(
                ()-> new ResourceNotFoundException("racerOfTeam", "id", racerOfTeamId)
        );

        Result result = resultRepo.findResultByRacerOfRaceTeamAndGrandPrix(racerOfRaceTeam, grandPrix);
        resultRepo.deleteById(result.getId());
        return true;
    }

}
