package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.Nation;
import com.example.demo.entity.f1.RaceTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INationRepo extends JpaRepository<Nation, Long> {
    Nation findByName(String national);
}
