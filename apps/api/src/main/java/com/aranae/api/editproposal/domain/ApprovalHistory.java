package com.aranae.api.editproposal.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import com.aranae.api.editproposal.type.Decision;
import com.aranae.api.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "approval_histories",
        uniqueConstraints = @UniqueConstraint(columnNames = "edit_proposal_id")
)
public class ApprovalHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "edit_proposal_id")
    private EditProposal editProposal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Decision decision;

    private String comment;

    protected ApprovalHistory() {
    }
}
