package com.metasis.ikamet.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProcessSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("pdate", table, columnPrefix + "_pdate"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("technical_indicators", table, columnPrefix + "_technical_indicators"));
        columns.add(Column.aliased("experience", table, columnPrefix + "_experience"));
        columns.add(Column.aliased("position", table, columnPrefix + "_position"));
        columns.add(Column.aliased("salary_expectation", table, columnPrefix + "_salary_expectation"));
        columns.add(Column.aliased("possible_assignmnet", table, columnPrefix + "_possible_assignmnet"));
        columns.add(Column.aliased("last_status", table, columnPrefix + "_last_status"));
        columns.add(Column.aliased("last_description", table, columnPrefix + "_last_description"));
        columns.add(Column.aliased("edit_by", table, columnPrefix + "_edit_by"));

        columns.add(Column.aliased("candidate_id", table, columnPrefix + "_candidate_id"));
        return columns;
    }
}
