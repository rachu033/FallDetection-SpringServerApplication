package com.af.springserver.repository;

import com.af.springserver.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findOutcomeIncidentsByUserId(Long userId);

    @Query("SELECT i FROM Incident i JOIN i.users u WHERE u.id = :userId")
    List<Incident> findIncomeIncidentsByUserId(Long userId);

    @Query("SELECT DISTINCT i FROM Incident i LEFT JOIN i.users u WHERE i.user.id = :userId OR u.id = :userId")
    List<Incident> findAllIncidentsByUserId(Long userId);
}
