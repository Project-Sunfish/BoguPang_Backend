package com.kill.gaebokchi.domain.bogu.entity.dto;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class EvolutionRequestDTO {
    private Long defaultBoguId;
    private List<String> categories;
    private String problem;

    public boolean hasNullFields() {
        return Objects.isNull(defaultBoguId) || Objects.isNull(categories) || Objects.isNull(problem) || StringUtils.isEmpty(problem);
    }
}
