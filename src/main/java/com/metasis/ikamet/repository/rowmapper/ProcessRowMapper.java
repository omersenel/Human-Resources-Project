package com.metasis.ikamet.repository.rowmapper;

import com.metasis.ikamet.domain.Process;
import com.metasis.ikamet.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Process}, with proper type conversions.
 */
@Service
public class ProcessRowMapper implements BiFunction<Row, String, Process> {

    private final ColumnConverter converter;

    public ProcessRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Process} stored in the database.
     */
    @Override
    public Process apply(Row row, String prefix) {
        Process entity = new Process();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPdate(converter.fromRow(row, prefix + "_pdate", Instant.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setTechnicalIndicators(converter.fromRow(row, prefix + "_technical_indicators", String.class));
        entity.setExperience(converter.fromRow(row, prefix + "_experience", String.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", String.class));
        entity.setSalaryExpectation(converter.fromRow(row, prefix + "_salary_expectation", Double.class));
        entity.setPossibleAssignmnet(converter.fromRow(row, prefix + "_possible_assignmnet", String.class));
        entity.setLastStatus(converter.fromRow(row, prefix + "_last_status", String.class));
        entity.setLastDescription(converter.fromRow(row, prefix + "_last_description", String.class));
        entity.setEditBy(converter.fromRow(row, prefix + "_edit_by", String.class));
        entity.setCandidateId(converter.fromRow(row, prefix + "_candidate_id", Long.class));
        return entity;
    }
}
