package com.aranae.api.editproposal.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import com.aranae.api.editproposal.type.ProposalStatus;
import com.aranae.api.editproposal.type.ProposalType;
import com.aranae.api.editproposal.type.TargetType;
import com.aranae.api.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "edit_proposals")
public class EditProposal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalType proposalType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    private Long targetId; // CREATE면 null 가능

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by")
    private User requestedBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String reason;

    // MySQL에서 json 타입을 쓰려면 columnDefinition 지정이 필요할 수 있음(초안)
    @Column(nullable = false, columnDefinition = "json")
    private String proposedData;

    @Column(nullable = false)
    private String dedupKey;

    protected EditProposal() {
    }

    // 상태 전이(초안)
    public void approve() {
        if (this.status != ProposalStatus.PENDING) {
            throw new IllegalStateException("Only PENDING proposal can be approved");
        }
        this.status = ProposalStatus.APPROVED;
    }

    public void reject() {
        if (this.status != ProposalStatus.PENDING) {
            throw new IllegalStateException("Only PENDING proposal can be rejected");
        }
        this.status = ProposalStatus.REJECTED;
    }
}
