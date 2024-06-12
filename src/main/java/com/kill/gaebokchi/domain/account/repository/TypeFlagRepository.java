package com.kill.gaebokchi.domain.account.repository;

import com.kill.gaebokchi.domain.account.entity.TypeFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeFlagRepository extends JpaRepository<TypeFlag, Long> {

}
