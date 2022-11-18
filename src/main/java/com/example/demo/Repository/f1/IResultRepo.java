package com.example.demo.Repository.f1;

import com.example.demo.dto.f1.RacerDto;
import com.example.demo.dto.f1.RacerResultDetail;
import com.example.demo.dto.f1.ResultRacerDto;
import com.example.demo.entity.f1.GrandPrix;
import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.RacerOfRaceTeam;
import com.example.demo.entity.f1.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IResultRepo extends JpaRepository<Result, Long> {
    @Query(value = "select \n" +
            "\n" +
            "\t\tRANK() OVER(order by \n" +
            "\t\t\t\t\ttotalPoints desc, totalTimes ) as ranking,\n" +
            "\t\tracer.id as racerId,\n" +
            "racer.name as racerName,\n" +
            "\t\trt.name as raceTeamName ,\n" +
            "        temp1.totalPoints as totalPoints,\n" +
            "\t\ttemp1.totalTimes as totalTimes,\n" +
            "        nation.name as national\n" +
            "        \n" +
            "        from(\n" +
            "\t\t\tselect *\n" +
            "\t\t\t, sum(res.point) totalPoints,\n" +
            "\t\t\tSEC_TO_TIME(SUM(TIME_TO_SEC(timediff(res.finish_time,res.start_time )))) totalTimes\n" +
            "\t\t\tfrom demo.tbl_result res\n" +
            "\t\t\tgroup by res.racer_of_team_id order by totalPoints desc\n" +
            ") temp1\n" +
            "inner join demo.tbl_racer_team rot on rot.id = temp1.racer_of_team_id \n" +
            "inner join demo.tbl_racer racer on rot.racer_id = racer.id\n" +
            "inner join demo.tbl_race_team rt on rot.race_team_id = rt.id\n" +
            "inner join demo.tbl_nation nation on nation.id = racer.national\n", nativeQuery = true)
    List<ResultRacerDto> findRacerRanking();

    @Query(value = "SELECT \n" +
            "\tRANK() OVER(order by \n" +
            "            totalPoints desc, totalTimes ) as ranking,\n" +
            "\tracer.id as racerId ,\n" +
            "    racer.name AS racerName,\n" +
            "    nation.name as national,\n" +
            "    rt.name AS raceTeamName,\n" +
            "    temp1.totalTimes as totalTimes,\n" +
            "    temp1.totalPoints as totalPoints\n" +
            "FROM\n" +
            "    (SELECT \n" +
            "        res.id AS id,\n" +
            "            res.racer_of_team_id AS racer_of_team_id,\n" +
            "            season.name name,\n" +
            "            SUM(res.point) totalPoints,\n" +
            "            SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(res.finish_time, res.start_time)))) totalTimes\n" +
            "    FROM\n" +
            "        demo.tbl_result res\n" +
            "    INNER JOIN demo.tbl_grand_prix grandPrix ON grandPrix.id = res.grand_prix_id\n" +
            "    INNER JOIN demo.tbl_season season ON grandPrix.season_id = :seasonId\n" +
            "    GROUP BY res.racer_of_team_id\n" +
            "    ORDER BY totalPoints DESC) temp1\n" +
            "        INNER JOIN\n" +
            "    demo.tbl_racer_team rot ON rot.id = temp1.racer_of_team_id\n" +
            "        INNER JOIN\n" +
            "    demo.tbl_racer racer ON rot.racer_id = racer.id\n" +
            "        INNER JOIN\n" +
            "    demo.tbl_race_team rt ON rot.race_team_id = rt.id\n" +
            "        left JOIN\n" +
            "    demo.tbl_nation nation ON nation.id = racer.national\n" +
            "\n",nativeQuery = true)
    List<ResultRacerDto> findRacerRanking(Long seasonId);

    @Query(value = "select\n" +
            "\tracer.id racerId\n" +
            "\t,\tracer.name racerName\n" +
            "\t, \tgp.name grandPrixName\n" +
            "    ,\tres.start_time time\n" +
            "    ,\tSEC_TO_TIME(TIME_TO_SEC(timediff(res.finish_time,res.start_time ))) finishTime\n" +
            "    ,\tres.point point\n" +
            "    ,\tres.ranking ranking\n" +
            "\t,\tseason.name \n" +
            " from demo.tbl_result res\n" +
            "inner join demo.tbl_grand_prix gp\n" +
            "on gp.id = res.grand_prix_id\n" +
            "inner join demo.tbl_racer_team rt\n" +
            "on rt.id = res.racer_of_team_id\n" +
            "inner join demo.tbl_racer racer\n" +
            "on racer.id = rt.racer_id\n" +
            "inner join demo.tbl_season season\n" +
            "on season.id = gp.season_id\n" +
            "where racer.id = :racerId " +
            "and season.id = :seasonId", nativeQuery = true)
    List<RacerResultDetail> findRacerResultDetail(Long racerId, Long seasonId);


    Result findResultByRacerOfRaceTeamAndGrandPrix(RacerOfRaceTeam racerOfRaceTeamId, GrandPrix grandPrixId);
}
