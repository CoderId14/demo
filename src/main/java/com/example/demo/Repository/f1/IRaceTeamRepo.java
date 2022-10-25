package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Racer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRaceTeamRepo extends JpaRepository<RaceTeam, Long> {
}
