package com.kill.gaebokchi.domain.user.entity;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="member")
public class Member extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy="host")
    private final List<DefaultBogu> bogus = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="typeFlag_id")
    private TypeFlag typeFlag;
    //==연관관계 메서드==//
    public void addBogus(DefaultBogu defaultBogu){
        bogus.add(defaultBogu);
        defaultBogu.setHost(this);
    }

    public void setFlag(TypeFlag setTypeFlag){
        typeFlag=setTypeFlag;
        setTypeFlag.setMember(this);
    }
}
