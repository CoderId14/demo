package com.example.demo.Service.f1.impl;


import com.example.demo.Repository.f1.*;
import com.example.demo.Service.f1.IRacerService;
import com.example.demo.api.f1.AddRacerToResult;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.IRacerDto;
import com.example.demo.dto.f1.RacerDto;
import com.example.demo.dto.f1.RacerOfTeamDto;
import com.example.demo.entity.f1.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class RacerOfTeamService {
    private final IRacerOfRaceTeamRepo racerOfRaceTeamRepo;

    private final IRaceTeamRepo raceTeamRepo;
    private final RacerRepo racerRepo;

    private final IGrandPrixRepo grandPrixRepo;
    private final ISeasonRepo seasonRepo;

    public List<RacerOfTeamDto> getAllRacerOfTeam(){
        List<RacerOfRaceTeam> racerOfRaceTeams = racerOfRaceTeamRepo.findAll();

        List<RacerOfTeamDto> racerOfTeamDtos = racerOfRaceTeams.stream().map(Mapper::toRacerOfTeamDto).collect(Collectors.toList());

        return racerOfTeamDtos;
    }

    public boolean addRacertoTeam(RacerOfTeamDto racerOfTeamDto){
        Racer racer = racerRepo.findById(racerOfTeamDto.getRacerId()).orElseThrow(
                () -> new ResourceNotFoundException("racer", "racer id", "racerOfTeam")
        );
        RaceTeam raceTeam = raceTeamRepo.findById(racerOfTeamDto.getRaceTeamId()).orElseThrow(
                () -> new ResourceNotFoundException("raceTeam", "raceTeam id", "racerOfTeam")
        );

         RacerOfRaceTeam racerOfRaceTeam  = RacerOfRaceTeam.builder()
                 .racer(racer)
                 .raceTeam(raceTeam)
                .build();
        racerOfRaceTeamRepo.save(racerOfRaceTeam);
        return true;
    }
    public List<IRacerDto> getRacerNotInGrandPrix(Long seasonId
            ,  Long grandPrixId
            ,  Long raceTeamId){
        RaceTeam raceTeam = raceTeamRepo.findById(raceTeamId).orElseThrow(
                () -> new ResourceNotFoundException("raceTeam", "raceTeam id", "racerOfTeam")
        );
        GrandPrix grandPrix = grandPrixRepo.findById(grandPrixId).orElseThrow(
                () -> new ResourceNotFoundException("grandPrix", "grandPrix id", grandPrixId)
        );
        Season season = seasonRepo.findById(seasonId).orElseThrow(
                () -> new ResourceNotFoundException("season", "season id", seasonId)
        );

        List<IRacerDto> racerList =racerOfRaceTeamRepo.findRacerNotInGrandPrix(seasonId,
                grandPrixId, raceTeamId);
        return racerList;
    }
    public List<IRacerDto> getRacerInGrandPrix(Long seasonId
            ,  Long grandPrixId
            ,  Long raceTeamId){
        RaceTeam raceTeam = raceTeamRepo.findById(raceTeamId).orElseThrow(
                () -> new ResourceNotFoundException("raceTeam", "raceTeam id", "racerOfTeam")
        );
        GrandPrix grandPrix = grandPrixRepo.findById(grandPrixId).orElseThrow(
                () -> new ResourceNotFoundException("grandPrix", "grandPrix id", grandPrixId)
        );
        Season season = seasonRepo.findById(seasonId).orElseThrow(
                () -> new ResourceNotFoundException("season", "season id", seasonId)
        );
        List<IRacerDto> racerList =racerOfRaceTeamRepo.findRacerInGrandPrix(seasonId,
                grandPrixId, raceTeamId);
        return racerList;
    }
//
//    public GrandPrixDto updateGrandPrix(GrandPrixDto grandPrixDto, Long id){
//        GrandPrix grandPrix = grandPrixRepo.findById(id).orElseThrow( () ->
//                new ResourceNotFoundException("grandPrix", "id", "id")
//        );
//        RaceCourse raceCourse = raceCourseRepo.findByName(grandPrixDto.getRaceCourse());
//        Season season = seasonRepo.findByName(grandPrixDto.getSeason()) ;
//        grandPrix.setName(grandPrixDto.getName());
//        grandPrix.setLaps(grandPrixDto.getLaps());
//        grandPrix.setDescription(grandPrixDto.getDescription());
//        grandPrix.setRaceCourse(raceCourse);
//        grandPrix.setSeason(season);
//        grandPrix.setTime(grandPrixDto.getTime());
//        grandPrixRepo.save(grandPrix);
//        return Mapper.toGrandPrixDto(grandPrix);
//    }
//
//    public boolean deleteGrandPrix(Long id){
//
//        GrandPrix racer = grandPrixRepo.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException("GrandPrix", "grandPrix id", "grandPrix id")
//        );
//
//        grandPrixRepo.delete(racer);
//
//        return true;
//    }
}
