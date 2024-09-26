package com.example.sinitto.member.entity;

import com.example.sinitto.guardGuideline.entity.GuardGuideLine;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "senior")
    private List<GuardGuideLine> guardGuideLines;

}
