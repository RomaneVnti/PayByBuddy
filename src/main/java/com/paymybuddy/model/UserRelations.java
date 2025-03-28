package com.paymybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "User_relations")
public class UserRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private int relationId;

    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    @Column(name = "relationship_status", nullable = false)
    private String relationshipStatus;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Getters and setters
    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
