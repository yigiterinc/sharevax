package com.sharevax.core.repository;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Harbor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarborRepository extends JpaRepository<Harbor, Integer> {
    Harbor findHarborByName(String name);
}