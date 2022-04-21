package com.metasis.ikamet.repository.rowmapper;

import com.metasis.ikamet.domain.File;
import com.metasis.ikamet.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link File}, with proper type conversions.
 */
@Service
public class FileRowMapper implements BiFunction<Row, String, File> {

    private final ColumnConverter converter;

    public FileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link File} stored in the database.
     */
    @Override
    public File apply(Row row, String prefix) {
        File entity = new File();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDataContentType(converter.fromRow(row, prefix + "_data_content_type", String.class));
        entity.setData(converter.fromRow(row, prefix + "_data", byte[].class));
        entity.setCandidateId(converter.fromRow(row, prefix + "_candidate_id", Long.class));
        return entity;
    }
}
