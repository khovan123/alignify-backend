/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.repository;

import com.api.model.SubscriptionTransactions;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Admin
 */
public interface ISubscriptionTransactionRepository extends MongoRepository<SubscriptionTransactions, String>{
    
}
