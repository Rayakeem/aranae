package com.aranae.api.celebrity.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "celebrities")
public class Celebrity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String groupName;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Integer height;

    private String profileImageKey;

    protected Celebrity() {
    }

    public static Celebrity create(String name, String groupName, JobType jobType, Integer height) {
        Celebrity celebrity = new Celebrity();
        celebrity.name = name;
        celebrity.groupName = groupName;
        celebrity.jobType = jobType;
        celebrity.height = height;
        return celebrity;
    }

    public void update(String name, String groupName, JobType jobType, Integer height, String profileImageKey) {
        this.name = name;
        this.groupName = groupName;
        this.jobType = jobType;
        this.height = height;
        this.profileImageKey = profileImageKey;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getGroupName() { return groupName; }
    public JobType getJobType() { return jobType; }
    public Integer getHeight() { return height; }
    public String getProfileImageKey() { return profileImageKey; }
}
