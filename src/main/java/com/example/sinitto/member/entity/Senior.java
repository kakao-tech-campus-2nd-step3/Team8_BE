package com.example.sinitto.member.entity;

import com.example.sinitto.guardGuideline.entity.GuardGuideLine;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;
    @OneToMany(mappedBy = "senior")
    private List<GuardGuideLine> guardGuideLines = new ArrayList<>();

}
