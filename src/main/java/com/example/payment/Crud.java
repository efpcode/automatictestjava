package com.example.payment;


import java.sql.SQLException;

public interface Crud {
    void executeUpdate( String sql) throws SQLException;
}
