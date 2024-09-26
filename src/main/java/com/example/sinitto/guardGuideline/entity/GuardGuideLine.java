package com.example.sinitto.guardGuideline.entity;

import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;

@Entity
public class GuardGuideLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    private Senior senior;

}
