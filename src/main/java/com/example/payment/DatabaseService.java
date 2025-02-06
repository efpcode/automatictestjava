package com.example.payment;

public interface DatabaseService {
    void executeUpdate (double amount, String status)throws DatabaseServiceException;

}
