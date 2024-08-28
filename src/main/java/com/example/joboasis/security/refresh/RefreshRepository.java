package com.example.joboasis.security.refresh;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
