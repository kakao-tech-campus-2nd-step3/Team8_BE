package com.example.sinitto.guardGuideline.entity;

import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class GuardGuideLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String content;
    @NotNull
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    @NotNull
    private Senior senior;

}
