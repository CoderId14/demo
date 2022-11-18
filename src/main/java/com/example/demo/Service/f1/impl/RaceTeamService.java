package com.example.demo.Service.f1.impl;

import com.example.demo.Repository.f1.IGrandPrixRepo;
import com.example.demo.Repository.f1.IRaceCourseRepo;
import com.example.demo.Repository.f1.IRaceTeamRepo;
import com.example.demo.Repository.f1.ISeasonRepo;
import com.example.demo.Service.f1.IGrandPrixService;
import com.example.demo.Service.f1.IRacerService;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.RaceTeamDto;
import com.example.demo.entity.f1.GrandPrix;
import com.example.demo.entity.f1.RaceCourse;
import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Season;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class RaceTeamService implements IRacerService {
    private final IRaceTeamRepo raceTeamRepo;
    private final ISeasonRepo seasonRepo;

    public List<RaceTeamDto> getAllRaceTeams(){
        List<RaceTeam> raceTeams = raceTeamRepo.findAll();

        List<RaceTeamDto> raceTeamDtos = raceTeams.stream().map(Mapper::toRaceTeamDto).collect(Collectors.toList());

        return raceTeamDtos;
    }
    public List<RaceTeamDto> getRaceTeamBySeason(Long seasonId){
        List<RaceTeam> raceTeams = raceTeamRepo.findBySeasons_ID(seasonId);
        List<RaceTeamDto> raceTeamDtos = raceTeams.stream().map(Mapper::toRaceTeamDto).collect(Collectors.toList());

        return raceTeamDtos;
    }
    public boolean addRaceTeam(RaceTeamDto raceTeamDto){


        Optional<RaceTeam> oldRaceTeam = raceTeamRepo.findById(raceTeamDto.getId());
        if(oldRaceTeam.isPresent()) throw new RuntimeException("Race team already added");
        RaceTeam raceTeam  = RaceTeam.builder()
                .name(raceTeamDto.getName())
                .description(raceTeamDto.getDescription())
                .powerUnit(raceTeamDto.getPowerUnit())
                .build();
        raceTeamRepo.save(raceTeam);
        return true;
    }

    public boolean addRaceTeamInSeason(Long id,RaceTeamDto raceTeamDto){

        Season season = seasonRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("season" ,"id", id)
        );
        Optional<RaceTeam> oldRaceTeam = raceTeamRepo.findById(raceTeamDto.getId());
        if(oldRaceTeam.isPresent() && oldRaceTeam.get().getSeasons().contains(season)) throw new RuntimeException("Race team already added");
        RaceTeam raceTeam  = RaceTeam.builder()
                .name(raceTeamDto.getName())
                .description(raceTeamDto.getDescription())
                .powerUnit(raceTeamDto.getPowerUnit())
                .build();
        Set<Season> newSeason = null;
        newSeason.add(season);
        raceTeam.setSeasons(newSeason);
        raceTeamRepo.save(raceTeam);
        return true;
    }

    public RaceTeamDto updateRaceTeamInSeason(Long raceTeamId, Long seasonId){
        RaceTeam raceTeam = raceTeamRepo.findById(raceTeamId).orElseThrow( () ->
           new ResourceNotFoundException("raceTean", "id", raceTeamId)
        );

        Season season = seasonRepo.findById(seasonId).orElseThrow( () ->
                new ResourceNotFoundException("season", "id", seasonId)
        );
        Set<RaceTeam> newRaceTeam = season.getRaceTeams();
        newRaceTeam.add(raceTeam);
        season.setRaceTeams(newRaceTeam);
        seasonRepo.save(season);
        return Mapper.toRaceTeamDto(raceTeam);
    }
    public  RaceTeamDto updateRaceTeam(Long id, RaceTeamDto raceTeamDto){
        RaceTeam raceTeam = raceTeamRepo.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("raceTean", "id", id)
        );
        raceTeam.setName(raceTeamDto.getName());
        raceTeam.setDescription(raceTeamDto.getDescription());
        raceTeam.setPowerUnit(raceTeamDto.getPowerUnit());
        raceTeamRepo.save(raceTeam);
        return Mapper.toRaceTeamDto(raceTeam);
    }

    public boolean deleteRaceTeamInSeason(Long id,RaceTeamDto raceTeamDto){
        RaceTeam raceTeam = raceTeamRepo.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("raceTeam", "id", id)
        );

        Season season = seasonRepo.findByName(raceTeamDto.getSeason());

        Set<Season> oldSeason = raceTeam.getSeasons();

        Set<Season> newSeason = oldSeason.stream().filter((item) -> item.getId() != season.getId()).collect(Collectors.toSet());
        raceTeam.setSeasons(newSeason);
        raceTeamRepo.save(raceTeam);
        return true;
    }
    public boolean deleteRaceTeam(Long raceTeamId){
        RaceTeam raceTeam = raceTeamRepo.findById(raceTeamId).orElseThrow( () ->
                new ResourceNotFoundException("raceTeam", "id", raceTeamId)
        );

        raceTeamRepo.delete(raceTeam);
        return true;
    }
}
