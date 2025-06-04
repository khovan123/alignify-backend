/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.repository;

import com.api.model.Likes;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends MongoRepository<Likes, String>{
     Optional<Likes> findByUserIdAndContentId(String userId, String contentId);
    long countByContentId(String contentId);
    void deleteByUserIdAndContentId(String userId, String contentId);
}
