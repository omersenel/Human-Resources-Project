package com.metasis.ikamet.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CandidateSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("first_name", table, columnPrefix + "_first_name"));
        columns.add(Column.aliased("last_name", table, columnPrefix + "_last_name"));
        columns.add(Column.aliased("university", table, columnPrefix + "_university"));
        columns.add(Column.aliased("graduation_year", table, columnPrefix + "_graduation_year"));
        columns.add(Column.aliased("gpa", table, columnPrefix + "_gpa"));
        columns.add(Column.aliased("edit_by", table, columnPrefix + "_edit_by"));

        return columns;
    }
}
