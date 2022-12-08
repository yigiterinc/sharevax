package com.sharevax.core.repository;

import com.sharevax.core.model.Harbor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarborRepository extends JpaRepository<Harbor, Integer> {
    @Query("SELECT h.id FROM Harbor h where h.country.id=:countryID")
    List<Integer> findHarborIDByCountryId(Integer countryID);
}