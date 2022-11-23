package com.sharevax.core.repository;

import com.sharevax.core.model.Harbor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarborRepository extends JpaRepository<Harbor, Integer> {
}