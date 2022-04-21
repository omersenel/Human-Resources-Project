package com.metasis.ikamet.repository.rowmapper;

import com.metasis.ikamet.domain.TechnicalInterview;
import com.metasis.ikamet.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TechnicalInterview}, with proper type conversions.
 */
@Service
public class TechnicalInterviewRowMapper implements BiFunction<Row, String, TechnicalInterview> {

    private final ColumnConverter converter;

    public TechnicalInterviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TechnicalInterview} stored in the database.
     */
    @Override
    public TechnicalInterview apply(Row row, String prefix) {
        TechnicalInterview entity = new TechnicalInterview();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", Instant.class));
        entity.setScore(converter.fromRow(row, prefix + "_score", Double.class));
        entity.setTechnicalStatus(converter.fromRow(row, prefix + "_technical_status", String.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setEditBy(converter.fromRow(row, prefix + "_edit_by", String.class));
        entity.setProcessId(converter.fromRow(row, prefix + "_process_id", Long.class));
        return entity;
    }
}
