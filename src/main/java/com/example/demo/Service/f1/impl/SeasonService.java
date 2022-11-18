package com.example.demo.Service.f1.impl;

import com.example.demo.Repository.f1.IRaceTeamRepo;
import com.example.demo.Repository.f1.ISeasonRepo;
import com.example.demo.Service.f1.ISeasonService;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.f1.RaceTeamDto;
import com.example.demo.dto.f1.SeasonDto;
import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Season;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Data
@Service
public class SeasonService implements ISeasonService {

    private final IRaceTeamRepo raceTeamRepo;

    private final ISeasonRepo seasonRepo;


    public Set<SeasonDto> getAllSeasons(){
        return seasonRepo.findAll().stream().map(Mapper::toSeasonDto).collect(Collectors.toSet());
    }

    public SeasonDto getSeason(Long id){
        Season season = seasonRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("season id", "id", id)
        );
        return Mapper.toSeasonDto(season);
    }
    public boolean addSeason(SeasonDto seasonDto){
        Season season = Season.builder()
                .name(seasonDto.getName())
                .build();
        seasonRepo.save(season);
        return true;
    }
    public boolean updateRaceTeamInSeason(Long raceTeamId, Long seasonId){
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
        return true;
    }
    public SeasonDto updateSeason(Long id, SeasonDto seasonDto){
        Season season = seasonRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("season id", "id", id)
        );
        season.setName(season.getName());
        seasonRepo.save(season);
        return Mapper.toSeasonDto(season);
    }
    public boolean deleteSeason(Long id){
        Season season = seasonRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("season id", "id", id)
        );
        seasonRepo.delete(season);
        return true;
    }
}
