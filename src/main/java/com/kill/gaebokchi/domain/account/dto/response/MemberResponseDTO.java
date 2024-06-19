package com.kill.gaebokchi.domain.account.dto.response;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.infra.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberResponseDTO {
    private SocialType socialType;
    private String name;
    private String birthType;
    private LocalDate birth;
    private String gender;

    public static MemberResponseDTO from(Member entity){

        return MemberResponseDTO.builder()
                .socialType(entity.getSocialType())
                .name(entity.getName())
                .birthType(entity.getBirthType())
                .birth(entity.getBirth())
                .gender(entity.getGender())
                .build();
    }
}
