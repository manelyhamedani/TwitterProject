package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class Table {
    public abstract void create() throws SQLException;

    protected void executeUpdate(String query) throws SQLException {
        Statement statement = SQL.getConnection().createStatement();
        statement.executeUpdate(query);
        statement.close();
    }


}
