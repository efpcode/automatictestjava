package com.example.payment;

public interface DatabaseService {
    void databaseUpdate(double amount, String status)throws DatabaseServiceException;

}
