package com.aranae.api.celebrity.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "celebrities")
public class Celebrity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String groupName;
    private String profileImageKey;

    protected Celebrity() {
    }
}
