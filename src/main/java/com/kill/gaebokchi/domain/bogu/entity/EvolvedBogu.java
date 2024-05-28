package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="evolved_bogu")
public class EvolvedBogu extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="evolved_bogu_id")
    private Long id;

    @OneToOne(mappedBy = "evolvedForm")
    private DefaultBogu defaultForm;

    @ElementCollection
    List<Category> categories;
    Category selectedCategory;
    private Integer variation;
    private String name;

    private Integer countBang;
    private Integer count;
    private LocalDateTime LastBangAt;

    private Integer level;
    private Integer status;
    @Column(length=1000)
    private String problem;
    private Boolean isLiberated;
    private LocalDateTime liberatedAt;
}
