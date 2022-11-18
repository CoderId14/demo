package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.GrandPrix;
import com.example.demo.entity.f1.RaceTeam;
import com.example.demo.entity.f1.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IGrandPrixRepo extends JpaRepository<GrandPrix, Long> {


    @Query("select gp from GrandPrix  gp where gp.season.id = :id ORDER BY gp.season.id desc ")
    List<GrandPrix> findGrandPrixBySeason_Id(Long id);
}
