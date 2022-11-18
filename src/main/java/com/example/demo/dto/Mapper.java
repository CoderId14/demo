package com.example.demo.dto;


import com.example.demo.dto.f1.*;
import com.example.demo.entity.User;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.entity.UserDto;
import com.example.demo.dto.response.JwtAuthenticationResponse;
import com.example.demo.entity.f1.*;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public static UserDto toUserDto(User user){
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(user.getRoles())
                .name(user.getName())
                .isActive(user.getIsActive())
                .createDate(user.getCreatedDate())
                .modifyDate(user.getModifiedDate())
                .build();
    }
    public static RacerDto toRacerDto(Racer racer){
        return RacerDto.builder()
                .id(racer.getId())
                .name(racer.getName())
                .bio(racer.getBio())
                .dateOfBirth(racer.getDateOfBirth())
                .national(racer.getNational().getName())
                .build();
    }
    public static GrandPrixDto toGrandPrixDto(GrandPrix grandPrix){
        return GrandPrixDto.builder()
                .id(grandPrix.getId())
                .name(grandPrix.getName())
                .laps(grandPrix.getLaps())
                .time(grandPrix.getTime())
                .description(grandPrix.getDescription())
                .raceCourse(grandPrix.getRaceCourse().getName())
                .season(grandPrix.getSeason().getName())
                .build();
    }

    public static RacerOfTeamDto toRacerOfTeamDto(RacerOfRaceTeam racerOfRaceTeam){
        return RacerOfTeamDto.builder()
                .racerId(racerOfRaceTeam.getRacer().getId())
                .racerName(racerOfRaceTeam.getRacer().getName())
                .raceTeamId(racerOfRaceTeam.getRaceTeam().getId())
                .raceTeamName(racerOfRaceTeam.getRaceTeam().getName())
                .build();
    }
    public static RaceTeamDto toRaceTeamDto(RaceTeam raceTeam){
        return RaceTeamDto.builder()
                .id(raceTeam.getId())
                .name(raceTeam.getName())
                .powerUnit(raceTeam.getPowerUnit())
                .description(raceTeam.getDescription())

                .build();
    }
    public static SeasonDto toSeasonDto(Season season){
        return SeasonDto.builder()
                .id(season.getId())
                .name(season.getName())


                .build();
    }
    public static Racer toRacer(RacerDto racerDto, Nation nation){

        return Racer.builder()
                .name(racerDto.getName())
                .bio(racerDto.getName())
                .dateOfBirth(racerDto.getDateOfBirth())
                .national(nation)
                .build();
    }

    public static JwtAuthenticationResponse toJwtAuthenticationRepsonse(String token, CustomUserDetails user){
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .username(user.getUsername())
                .build();
    }
}
