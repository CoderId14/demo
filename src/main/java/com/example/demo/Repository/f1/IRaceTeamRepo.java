package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Racer;
import com.example.demo.entity.f1.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface IRaceTeamRepo extends JpaRepository<RaceTeam, Long> {
    @Query("select rt from RaceTeam rt join fetch rt.seasons where rt.id = :id")
    List<RaceTeam> findBySeasons_ID(Long id);
}
