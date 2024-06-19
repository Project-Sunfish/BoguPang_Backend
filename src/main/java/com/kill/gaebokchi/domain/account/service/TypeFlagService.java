package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.entity.TypeFlag;
import com.kill.gaebokchi.domain.account.repository.TypeFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class TypeFlagService {
    private final TypeFlagRepository typeFlagRepository;

    @Transactional
    public TypeFlag createFlag(){
        TypeFlag typeFlag = new TypeFlag();
        List<Boolean> flag = new ArrayList<>(Collections.nCopies(23, false));
        typeFlag.setNewFlag(flag);
        typeFlag.setLiberatedFlag(flag);
        typeFlagRepository.save(typeFlag);
        return typeFlag;
    }
}
