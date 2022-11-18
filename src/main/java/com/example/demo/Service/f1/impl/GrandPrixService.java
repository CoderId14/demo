package com.example.demo.Service.f1.impl;

import com.example.demo.Repository.f1.*;
import com.example.demo.Service.f1.IGrandPrixService;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.f1.GrandPrixDto;
import com.example.demo.dto.f1.RacerDto;
import com.example.demo.entity.f1.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class GrandPrixService implements IGrandPrixService {
    private final IGrandPrixRepo grandPrixRepo;

    private final IRaceCourseRepo raceCourseRepo;
    private final ISeasonRepo seasonRepo;

    public List<GrandPrixDto> getAllGrandPrixes(){
        List<GrandPrix> grandPrixList = grandPrixRepo.findAll();

        List<GrandPrixDto> grandPrixDtos = grandPrixList.stream().map(Mapper::toGrandPrixDto).collect(Collectors.toList());

        return grandPrixDtos;
    }
    public List<GrandPrixDto> getAllGrandPrixesBySeason(Long seasonId){
        List<GrandPrix> grandPrixList = grandPrixRepo.findGrandPrixBySeason_Id(seasonId);

        List<GrandPrixDto> grandPrixDtos = grandPrixList.stream().map(Mapper::toGrandPrixDto).collect(Collectors.toList());

        return grandPrixDtos;
    }


    public boolean addGrandPrix(GrandPrixDto grandPrixDto){

        RaceCourse raceCourse = raceCourseRepo.findByName(grandPrixDto.getRaceCourse());
        Season season = seasonRepo.findByName(grandPrixDto.getSeason()) ;
        GrandPrix grandPrix  = GrandPrix.builder()
                .name(grandPrixDto.getName())
                .description(grandPrixDto.getDescription())
                .laps(grandPrixDto.getLaps())
                .raceCourse(raceCourse)
                .season(season)
                .time(grandPrixDto.getTime())
                .build();
        grandPrixRepo.save(grandPrix);
        return true;
    }

    public GrandPrixDto updateGrandPrix(GrandPrixDto grandPrixDto, Long id){
        GrandPrix grandPrix = grandPrixRepo.findById(id).orElseThrow( () ->
           new ResourceNotFoundException("grandPrix", "id", "id")
        );
        RaceCourse raceCourse = raceCourseRepo.findByName(grandPrixDto.getRaceCourse());
        Season season = seasonRepo.findByName(grandPrixDto.getSeason()) ;
        grandPrix.setName(grandPrixDto.getName());
        grandPrix.setLaps(grandPrixDto.getLaps());
        grandPrix.setDescription(grandPrixDto.getDescription());
        grandPrix.setRaceCourse(raceCourse);
        grandPrix.setSeason(season);
        grandPrix.setTime(grandPrixDto.getTime());
        grandPrixRepo.save(grandPrix);
        return Mapper.toGrandPrixDto(grandPrix);
    }

    public boolean deleteGrandPrix(Long id){

        GrandPrix grandPrix = grandPrixRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("GrandPrix", "grandPrix id", "grandPrix id")
        );

        grandPrixRepo.delete(grandPrix);

        return true;
    }
}
