package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="default_bogu")
public class DefaultBogu extends BaseTime implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="default_bogu_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member host;

    @OneToOne
    @JoinColumn(name="evolvedBogu_id")
    private EvolvedBogu evolvedForm;

    //==연관관계 메서드==//
    public void addEvolvedForm(EvolvedBogu evolvedBogu){
        evolvedForm=evolvedBogu;
        evolvedBogu.setDefaultForm(this);
    }
}
