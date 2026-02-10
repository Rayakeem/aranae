package com.aranae.api.user.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String role; // USER, ADMIN (추후 enum으로 확장 가능)

    protected User() {
    }
}
