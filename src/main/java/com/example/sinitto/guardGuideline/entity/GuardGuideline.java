package com.example.sinitto.guardGuideline.entity;

import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class GuardGuideline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Senior senior;

    protected GuardGuideline() {
    }

    public GuardGuideline(Type type, String title, String content, Senior senior) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.senior = senior;
    }


    public void updateGuardGuideline(Type type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Senior getSenior() {
        return senior;
    }


    public enum Type {
        TAXI,
        DELIVERY
    }
}
