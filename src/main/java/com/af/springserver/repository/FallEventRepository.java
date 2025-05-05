package com.af.springserver.repository;

import com.af.springserver.model.FallEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FallEventRepository extends JpaRepository<FallEvent, Long> {
    List<FallEvent> findByUserId(Long userId);
}
