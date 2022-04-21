package com.metasis.ikamet.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Process.
 */
@Table("process")
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("pdate")
    private Instant pdate;

    @Column("department")
    private String department;

    @Column("technical_indicators")
    private String technicalIndicators;

    @Column("experience")
    private String experience;

    @Column("position")
    private String position;

    @Column("salary_expectation")
    private Double salaryExpectation;

    @Column("possible_assignmnet")
    private String possibleAssignmnet;

    @Column("last_status")
    private String lastStatus;

    @Column("last_description")
    private String lastDescription;

    @Column("edit_by")
    private String editBy;

    @Transient
    private Candidate candidate;

    @Column("candidate_id")
    private Long candidateId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Process id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPdate() {
        return this.pdate;
    }

    public Process pdate(Instant pdate) {
        this.setPdate(pdate);
        return this;
    }

    public void setPdate(Instant pdate) {
        this.pdate = pdate;
    }

    public String getDepartment() {
        return this.department;
    }

    public Process department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTechnicalIndicators() {
        return this.technicalIndicators;
    }

    public Process technicalIndicators(String technicalIndicators) {
        this.setTechnicalIndicators(technicalIndicators);
        return this;
    }

    public void setTechnicalIndicators(String technicalIndicators) {
        this.technicalIndicators = technicalIndicators;
    }

    public String getExperience() {
        return this.experience;
    }

    public Process experience(String experience) {
        this.setExperience(experience);
        return this;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPosition() {
        return this.position;
    }

    public Process position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalaryExpectation() {
        return this.salaryExpectation;
    }

    public Process salaryExpectation(Double salaryExpectation) {
        this.setSalaryExpectation(salaryExpectation);
        return this;
    }

    public void setSalaryExpectation(Double salaryExpectation) {
        this.salaryExpectation = salaryExpectation;
    }

    public String getPossibleAssignmnet() {
        return this.possibleAssignmnet;
    }

    public Process possibleAssignmnet(String possibleAssignmnet) {
        this.setPossibleAssignmnet(possibleAssignmnet);
        return this;
    }

    public void setPossibleAssignmnet(String possibleAssignmnet) {
        this.possibleAssignmnet = possibleAssignmnet;
    }

    public String getLastStatus() {
        return this.lastStatus;
    }

    public Process lastStatus(String lastStatus) {
        this.setLastStatus(lastStatus);
        return this;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getLastDescription() {
        return this.lastDescription;
    }

    public Process lastDescription(String lastDescription) {
        this.setLastDescription(lastDescription);
        return this;
    }

    public void setLastDescription(String lastDescription) {
        this.lastDescription = lastDescription;
    }

    public String getEditBy() {
        return this.editBy;
    }

    public Process editBy(String editBy) {
        this.setEditBy(editBy);
        return this;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
        this.candidateId = candidate != null ? candidate.getId() : null;
    }

    public Process candidate(Candidate candidate) {
        this.setCandidate(candidate);
        return this;
    }

    public Long getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(Long candidate) {
        this.candidateId = candidate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Process)) {
            return false;
        }
        return id != null && id.equals(((Process) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Process{" +
            "id=" + getId() +
            ", pdate='" + getPdate() + "'" +
            ", department='" + getDepartment() + "'" +
            ", technicalIndicators='" + getTechnicalIndicators() + "'" +
            ", experience='" + getExperience() + "'" +
            ", position='" + getPosition() + "'" +
            ", salaryExpectation=" + getSalaryExpectation() +
            ", possibleAssignmnet='" + getPossibleAssignmnet() + "'" +
            ", lastStatus='" + getLastStatus() + "'" +
            ", lastDescription='" + getLastDescription() + "'" +
            ", editBy='" + getEditBy() + "'" +
            "}";
    }
}
