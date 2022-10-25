package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.GrandPrix;
import com.example.demo.entity.f1.RaceTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGrandPrixRepo extends JpaRepository<GrandPrix, Long> {
}
