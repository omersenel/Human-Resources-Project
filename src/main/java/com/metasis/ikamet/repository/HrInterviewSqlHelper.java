package com.metasis.ikamet.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HrInterviewSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("score", table, columnPrefix + "_score"));
        columns.add(Column.aliased("ik_status", table, columnPrefix + "_ik_status"));
        columns.add(Column.aliased("notes", table, columnPrefix + "_notes"));
        columns.add(Column.aliased("edit_by", table, columnPrefix + "_edit_by"));

        columns.add(Column.aliased("process_id", table, columnPrefix + "_process_id"));
        return columns;
    }
}
