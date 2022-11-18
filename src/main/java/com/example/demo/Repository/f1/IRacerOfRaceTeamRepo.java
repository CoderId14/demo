package com.example.demo.Repository.f1;

import com.example.demo.dto.f1.IRacerDto;
import com.example.demo.dto.f1.RacerDto;
import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Racer;
import com.example.demo.entity.f1.RacerOfRaceTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRacerOfRaceTeamRepo extends JpaRepository<RacerOfRaceTeam, Long> {

    @Query(value = "select \n" +
            "\tracer.id as id,\n" +
            "    racer.bio as bio,\n" +
            "    racer.name as name,\n" +
            "    nation.name as national,\n" +
            "    racer.date_of_birth as dateOfBirth,\n" +
            "    temp1.id as racerOfTeamId" +
            " from \n" +
            "\t(select * from demo.tbl_racer_team rot\n" +
            "where rot.race_team_id =:raceTeamId and rot.status =1\n" +
            "and rot.racer_id not in (\n" +
            "select racer.id from demo.tbl_result res\n" +
            "inner join demo.tbl_racer_team rot on rot.id = res.racer_of_team_id \n" +
            "inner join demo.tbl_racer racer on rot.racer_id = racer.id\n" +
            "inner join demo.tbl_race_team rt on rot.race_team_id = rt.id\n" +
            "inner join demo.tbl_grand_prix gp on gp.id = res.grand_prix_id\n" +
            "inner join demo.tbl_season season on season.id = gp.season_id\n" +
            "where season.id = :seasonId and rot.race_team_id =:raceTeamId and gp.id = :grandPrixId\n" +
            ") \n" +
            ")temp1\n" +
            "inner join demo.tbl_racer racer on temp1.racer_id = racer.id\n" +
            "inner join demo.tbl_nation nation on racer.national = nation.id", nativeQuery = true)
    List<IRacerDto> findRacerNotInGrandPrix(Long seasonId, Long grandPrixId, Long raceTeamId);

    @Query(value = "select racer.id as id,\n" +
            "    racer.bio as bio,\n" +
            "    racer.name as name,\n" +
            "    nation.name as national,\n" +
            "    racer.date_of_birth as dateOfBirth,\n" +
            "    res.racer_of_team_id as racerOfTeamId" +
            "    from demo.tbl_result res\n" +
            "inner join demo.tbl_racer_team rot on rot.id = res.racer_of_team_id \n" +
            "inner join demo.tbl_racer racer on rot.racer_id = racer.id\n" +
            "inner join demo.tbl_race_team rt on rot.race_team_id = rt.id\n" +
            "inner join demo.tbl_grand_prix gp on gp.id = res.grand_prix_id\n" +
            "inner join demo.tbl_season season on season.id = gp.season_id\n" +
            "inner join demo.tbl_nation nation on racer.national = nation.id\n" +
            "where season.id = :seasonId and rot.race_team_id =:raceTeamId and gp.id = :grandPrixId", nativeQuery = true)
    List<IRacerDto> findRacerInGrandPrix(Long seasonId, Long grandPrixId, Long raceTeamId);

    RacerOfRaceTeam findByRaceTeam(RaceTeam raceTeam);
}
