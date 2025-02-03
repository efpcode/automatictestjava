package com.example.payment;

import java.sql.SQLException;

public class MockDataBaseConnection implements DatabaseConnection {

    private static MockDataBaseConnection connection  = null;
    private final boolean dbConnection;
    private boolean dbCommit;
    private String queryStatement;


    private  MockDataBaseConnection() {
        this.dbConnection = true;
        this.dbCommit = false;
        this.queryStatement = "";

    }

    public static MockDataBaseConnection getInstance() {
        if(connection == null) {
            connection = new MockDataBaseConnection();
        }
        return connection;
    }

    @Override
    public void executeUpdate(String sql) throws SQLException {
        if(!connection.dbConnection) {
            throw new SQLException("Database connection is not active");

        }
        this.queryStatement = sql;
        this.dbCommit = true;

    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public boolean verify(){
        return this.dbConnection;
    }



}
