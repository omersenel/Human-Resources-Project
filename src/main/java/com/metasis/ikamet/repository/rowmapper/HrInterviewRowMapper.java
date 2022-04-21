package com.metasis.ikamet.repository.rowmapper;

import com.metasis.ikamet.domain.HrInterview;
import com.metasis.ikamet.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HrInterview}, with proper type conversions.
 */
@Service
public class HrInterviewRowMapper implements BiFunction<Row, String, HrInterview> {

    private final ColumnConverter converter;

    public HrInterviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HrInterview} stored in the database.
     */
    @Override
    public HrInterview apply(Row row, String prefix) {
        HrInterview entity = new HrInterview();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", Instant.class));
        entity.setScore(converter.fromRow(row, prefix + "_score", Double.class));
        entity.setIkStatus(converter.fromRow(row, prefix + "_ik_status", String.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setEditBy(converter.fromRow(row, prefix + "_edit_by", String.class));
        entity.setProcessId(converter.fromRow(row, prefix + "_process_id", Long.class));
        return entity;
    }
}
