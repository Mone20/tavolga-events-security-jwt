package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(	name = "assessments",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"contest_id", "nomination_id", "criterion_id" }) }
)
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    public Long id;

    @JsonView(Views.Public.class)
    public Integer assessment;

    @JsonView(Views.Private.class)
    @ManyToOne
    public Nomination nomination;

    @JsonView(Views.Private.class)
    @ManyToOne
    public Criterion criterion;

    @JsonView(Views.Private.class)
    @ManyToOne
    public Contest contest;

    @JsonView(Views.Private.class)
    @ManyToOne
    public User participant;

    @JsonView(Views.Public.class)
    @ManyToOne
    public User assessor;

    @JsonView(Views.Public.class)
    public String comment;

    @JsonView(Views.Public.class)
    public Date approvalDateTime;

    public Assessment() {
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssessment() {
        return assessment;
    }

    public void setAssessment(Integer assessment) {
        this.assessment = assessment;
    }

    public Nomination getNomination() {
        return nomination;
    }

    public void setNomination(Nomination nomination) {
        this.nomination = nomination;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public User getAssessor() {
        return assessor;
    }

    public void setAssessor(User assessor) {
        this.assessor = assessor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(Date approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }
}
