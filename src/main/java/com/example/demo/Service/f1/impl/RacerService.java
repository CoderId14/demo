package com.example.demo.Service.f1.impl;


import com.example.demo.Repository.f1.INationRepo;
import com.example.demo.Repository.f1.RacerRepo;
import com.example.demo.Service.f1.IRacerService;
import com.example.demo.dto.Mapper;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.f1.RacerDto;
import com.example.demo.entity.f1.Nation;
import com.example.demo.entity.f1.Racer;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.Utils.Constant.ROLE_USER;

@Service
@Data
public class RacerService implements IRacerService {
    private final RacerRepo racerRepo;

    private final INationRepo nationRepo;

    public List<RacerDto> getAllRacers(){
        List<Racer> racerList = racerRepo.findAll();

        List<RacerDto> racerDtoList = racerList.stream().map(Mapper::toRacerDto).collect(Collectors.toList());

        return racerDtoList;
    }

    public boolean addRacer(RacerDto racerDto){

        Nation nation = nationRepo.findByName(racerDto.getNational());

        racerRepo.save(Mapper.toRacer(racerDto, nation));

        return true;
    }

    public RacerDto updateRacer(RacerDto racerDto, Long id){

        Nation nation = nationRepo.findByName(racerDto.getNational());


        Racer racer = racerRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Racer", "Racer id", "Racer id")
        );
        racer.setName(racerDto.getName());
        racer.setBio(racerDto.getBio());
        racer.setNational(nation);
        racer.setDateOfBirth(racerDto.getDateOfBirth());
        racerRepo.save(racer);
        return Mapper.toRacerDto(racer);
    }
    public RacerDto[] updateRacers(RacerDto[] racerDtos){

        Arrays.stream(racerDtos).map((racer) -> updateRacer(racer,racer.getId()));

        return racerDtos;
    }
    public boolean deleteRacer(Long id){

        Racer racer = racerRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Racer", "Racer id", "Racer id")
        );

        racerRepo.delete(racer);

        return true;
    }


}
