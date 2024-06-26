package com.kill.gaebokchi.domain.account.dto.response;

import com.kill.gaebokchi.domain.account.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TutorialResponseDTO {
    private Boolean tutorialFlag;
    public static TutorialResponseDTO from(Member entity){

        return TutorialResponseDTO.builder()
                .tutorialFlag(entity.getTutorial())
                .build();
    }
}
