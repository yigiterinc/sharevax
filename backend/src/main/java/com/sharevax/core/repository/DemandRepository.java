package com.sharevax.core.repository;

import com.sharevax.core.model.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {

    // An unmatched demand is a demand which is not present in the delivery table
    @Query("SELECT d FROM Demand d WHERE d.quantity>0 and d.id NOT IN (SELECT suggestion.demand.id FROM Suggestion suggestion)")
    List<Demand> findUnmatchedDemands();
}