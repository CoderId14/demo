package com.example.demo.Repository.f1;

import com.example.demo.entity.f1.City;
import com.example.demo.entity.f1.RaceTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICityRepo extends JpaRepository<City, Long> {
}
