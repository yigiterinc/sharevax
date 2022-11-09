package com.sharevax.core.repository;

import com.sharevax.core.model.Virus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirusRepository extends JpaRepository<Virus, Integer> {
}