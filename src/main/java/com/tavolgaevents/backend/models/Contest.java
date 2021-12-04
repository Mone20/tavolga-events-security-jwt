package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(	name = "contests",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
        )
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    public Long id;

    @NotBlank
    @JsonView(Views.Public.class)
    public String name;

    @JsonView(Views.Public.class)
    public String description;

    @JsonView(Views.Private.class)
    @ManyToMany(fetch = FetchType.LAZY)
    List<Nomination> nominations;

    @JsonView(Views.Public.class)
    @OneToMany(fetch = FetchType.LAZY)
    List<ContestPart> contestParts;

    @JsonView(Views.Private.class)
    @OneToMany(fetch = FetchType.LAZY)
     List<Assessment> assessments;

    public Contest() {
    }

    public List<ContestPart> getContestParts() {
        return contestParts;
    }

    public void setContestParts(List<ContestPart> contestParts) {
        this.contestParts = contestParts;
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

    public List<Nomination> getNominations() {
        return nominations;
    }

    public void setNominations(List<Nomination> nominations) {
        this.nominations = nominations;
    }
}
