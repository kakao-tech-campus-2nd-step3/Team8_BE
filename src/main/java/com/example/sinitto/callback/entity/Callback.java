package com.example.sinitto.callback.entity;

import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    @NotNull
    private Senior senior;

}
