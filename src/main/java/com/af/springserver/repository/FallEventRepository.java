package com.af.springserver.repository;

import com.af.springserver.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FallEventRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByUserId(Long userId);
}
