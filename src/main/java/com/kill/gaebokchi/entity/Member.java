package com.kill.gaebokchi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter @Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member{
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private Date birth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;

    private String refreshToken;

}
