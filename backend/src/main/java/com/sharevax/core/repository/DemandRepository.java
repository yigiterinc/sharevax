package com.sharevax.core.repository;

import com.sharevax.core.model.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, Integer> {
}