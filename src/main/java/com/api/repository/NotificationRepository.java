package com.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

  Page<Notification> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

}
