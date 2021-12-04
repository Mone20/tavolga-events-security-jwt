package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(	name = "criterion")
public class Criterion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    public Long id;

    @JsonView(Views.Public.class)
    @NotBlank
    public String name;

    @JsonView(Views.Public.class)
    @NotBlank
    public String description;

    @JsonView(Views.Public.class)
    @NotBlank
    public Integer maxAssessment;

    @JsonView(Views.Private.class)
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Nomination> nominations;

    @JsonView(Views.Private.class)
    @OneToMany(mappedBy = "criterion", fetch = FetchType.LAZY)
        List<Assessment> assessments;
    public Criterion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxAssessment() {
        return maxAssessment;
    }

    public void setMaxAssessment(Integer maxAssessment) {
        this.maxAssessment = maxAssessment;
    }

    public List<Nomination> getNominations() {
        return nominations;
    }

    public void setNominations(List<Nomination> nominations) {
        this.nominations = nominations;
    }
}
