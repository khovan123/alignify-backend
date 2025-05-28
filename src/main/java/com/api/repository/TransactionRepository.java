/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.repository;

import com.api.model.Transaction;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Admin
 */
public interface TransactionRepository extends MongoRepository<Transaction, String>{
    List<Transaction> findByUserId(String userId);
}
