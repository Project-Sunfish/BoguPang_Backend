package com.kill.gaebokchi.domain.bogu.dto.response.dogam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DogamBoguResponseDTO {
    Integer countKinds;
    List<CollectedBoguResponseDTO> collectedBogus;
}
