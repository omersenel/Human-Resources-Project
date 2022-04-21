package com.metasis.ikamet.repository.rowmapper;

import com.metasis.ikamet.domain.Candidate;
import com.metasis.ikamet.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Candidate}, with proper type conversions.
 */
@Service
public class CandidateRowMapper implements BiFunction<Row, String, Candidate> {

    private final ColumnConverter converter;

    public CandidateRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Candidate} stored in the database.
     */
    @Override
    public Candidate apply(Row row, String prefix) {
        Candidate entity = new Candidate();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFirstName(converter.fromRow(row, prefix + "_first_name", String.class));
        entity.setLastName(converter.fromRow(row, prefix + "_last_name", String.class));
        entity.setUniversity(converter.fromRow(row, prefix + "_university", String.class));
        entity.setGraduationYear(converter.fromRow(row, prefix + "_graduation_year", String.class));
        entity.setGpa(converter.fromRow(row, prefix + "_gpa", Double.class));
        entity.setEditBy(converter.fromRow(row, prefix + "_edit_by", String.class));
        return entity;
    }
}
