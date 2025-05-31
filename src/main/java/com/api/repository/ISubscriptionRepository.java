/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.repository;

import com.api.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ISubscriptionRepository extends MongoRepository<Subscription, String>{
    
}
