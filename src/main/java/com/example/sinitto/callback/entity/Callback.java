package com.example.sinitto.callback.entity;

import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Callback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime postTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CallbackStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Senior senior;

    public Callback(CallbackStatus status, Senior senior) {
        this.status = status;
        this.senior = senior;
    }

    public Callback() {

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public String getStatus() {
        return status.name();
    }

    public String getSeniorName() {
        return senior.getName();
    }

    public Long getSeniorId() {
        return senior.getId();
    }

}
