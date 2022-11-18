package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISeasonRepo extends JpaRepository<Season, Long> {
    Season findByName(String season);
}
