package com.sharevax.core.repository;

import com.sharevax.core.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    @Query("SELECT d FROM Delivery d WHERE d.deliveryStatus <> 'DELIVERED'")
    List<Delivery> findActiveDeliveries();
}