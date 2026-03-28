package com.aranae.api.history.domain;

import com.aranae.api.common.domain.TargetType;
import com.aranae.api.user.domain.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "edit_histories")
@EntityListeners(AuditingEntityListener.class)
public class EditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "editor_id")
    private User editor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EditAction action;

    @Column(columnDefinition = "json")
    private String beforeData;  // CREATE면 null

    @Column(columnDefinition = "json")
    private String afterData;   // DELETE면 null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected EditHistory() {
    }

    public static EditHistory of(User editor, TargetType targetType, Long targetId,
                                 EditAction action, String beforeData, String afterData) {
        EditHistory history = new EditHistory();
        history.editor = editor;
        history.targetType = targetType;
        history.targetId = targetId;
        history.action = action;
        history.beforeData = beforeData;
        history.afterData = afterData;
        return history;
    }

    public Long getId() {
        return id;
    }

    public User getEditor() {
        return editor;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public EditAction getAction() {
        return action;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
