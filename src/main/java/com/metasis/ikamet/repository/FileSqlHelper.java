package com.metasis.ikamet.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FileSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("data", table, columnPrefix + "_data"));
        columns.add(Column.aliased("data_content_type", table, columnPrefix + "_data_content_type"));

        columns.add(Column.aliased("candidate_id", table, columnPrefix + "_candidate_id"));
        return columns;
    }
}
