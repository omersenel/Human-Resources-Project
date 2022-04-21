package com.metasis.ikamet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HrInterview.
 */
@Table("hr_interview")
public class HrInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("date")
    private Instant date;

    @Column("score")
    private Double score;

    @Column("ik_status")
    private String ikStatus;

    @Column("notes")
    private String notes;

    @Column("edit_by")
    private String editBy;

    @Transient
    @JsonIgnoreProperties(value = { "candidate" }, allowSetters = true)
    private Process process;

    @Column("process_id")
    private Long processId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HrInterview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public HrInterview date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getScore() {
        return this.score;
    }

    public HrInterview score(Double score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getIkStatus() {
        return this.ikStatus;
    }

    public HrInterview ikStatus(String ikStatus) {
        this.setIkStatus(ikStatus);
        return this;
    }

    public void setIkStatus(String ikStatus) {
        this.ikStatus = ikStatus;
    }

    public String getNotes() {
        return this.notes;
    }

    public HrInterview notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEditBy() {
        return this.editBy;
    }

    public HrInterview editBy(String editBy) {
        this.setEditBy(editBy);
        return this;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public Process getProcess() {
        return this.process;
    }

    public void setProcess(Process process) {
        this.process = process;
        this.processId = process != null ? process.getId() : null;
    }

    public HrInterview process(Process process) {
        this.setProcess(process);
        return this;
    }

    public Long getProcessId() {
        return this.processId;
    }

    public void setProcessId(Long process) {
        this.processId = process;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HrInterview)) {
            return false;
        }
        return id != null && id.equals(((HrInterview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HrInterview{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", score=" + getScore() +
            ", ikStatus='" + getIkStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", editBy='" + getEditBy() + "'" +
            "}";
    }
}
