package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.RacerOfRaceTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRacerOfRaceTeamRepo extends JpaRepository<RacerOfRaceTeam, Long> {
}
