package com.kill.gaebokchi.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="type_flag")
public class TypeFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="type_flag_id")
    private Long id;

    @OneToOne(mappedBy="typeFlag")
    private Member member;

    List<Boolean> newFlag;
    List<Boolean> liberatedFlag;
}
