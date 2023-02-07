package com.sharevax.core.repository;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Suggestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {

    @Query("SELECT s FROM Suggestion s WHERE s.supply.country.id = ?1 or s.demand.country.id=?1")
    List<Suggestion> findRelatedSuggestions(Integer countryId);

    Optional<Suggestion> findById(Integer id);
}