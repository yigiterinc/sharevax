package com.sharevax.core.repository;

import com.sharevax.core.model.Harbor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarborRepository extends JpaRepository<Harbor, Integer> {
}