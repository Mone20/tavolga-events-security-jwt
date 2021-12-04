package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(	name = "nominations")
public class Nomination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    public Long id;

    @NotBlank
    @JsonView(Views.Public.class)
    public String name;

    public String description;

    @ManyToMany(mappedBy = "nominations",fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    List<Contest> contests;

    @ManyToMany(mappedBy = "nominations",fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    List<Criterion> criterionList;

    @ManyToMany(mappedBy = "nominations",fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    private List<User> participants;

    @ManyToMany(mappedBy = "rateNominations",fetch = FetchType.LAZY)
    @JsonView(Views.Public.class)
    private List<User> ratingUsers;

    @JsonView(Views.Public.class)
    @OneToMany(mappedBy = "nomination", fetch = FetchType.LAZY)
        List<Assessment> assessments;

    public Nomination() {
    }

    public List<User> getRatingUsers() {
        return ratingUsers;
    }

    public void setRatingUsers(List<User> ratingUsers) {
        this.ratingUsers = ratingUsers;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> users) {
        this.participants = users;
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

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }

    public List<Criterion> getCriterionList() {
        return criterionList;
    }

    public void setCriterionList(List<Criterion> criterionList) {
        this.criterionList = criterionList;
    }
}
