package com.sharevax.core.repository;

import com.sharevax.core.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Integer> {

    @Query("SELECT s FROM Supply s WHERE s.id NOT IN (SELECT delivery.supply.id FROM Delivery delivery)")
    List<Supply> findUnmatchedSupplies();
}