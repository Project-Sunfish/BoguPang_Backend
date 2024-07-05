package com.kill.gaebokchi.domain.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopTimeRepository extends JpaRepository<PopTime, Long> {
}
