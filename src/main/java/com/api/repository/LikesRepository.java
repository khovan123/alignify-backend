
package com.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Likes;

@Repository
public interface LikesRepository extends MongoRepository<Likes, String> {
    Optional<Likes> findByUserIdAndContentId(String userId, String contentId);

    long countByContentId(String contentId);

    void deleteByUserIdAndContentId(String userId, String contentId);

    void deleteAllByContentId(String contentId);

    boolean existsByContentIdAndUserId(String contentId, String userId);
}
