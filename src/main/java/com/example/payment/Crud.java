package com.example.payment;


import java.sql.SQLException;

public interface Crud {
    void executeUpdate( double amount, String status) throws SQLException;
}
