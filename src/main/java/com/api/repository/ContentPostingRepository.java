
package com.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.ContentPosting;
import org.springframework.data.domain.Page;

@Repository
public interface ContentPostingRepository extends MongoRepository<ContentPosting, String>{
    Page<ContentPosting> findByUserId(String userId, Pageable pageable);
    Page<ContentPosting> findByUserIdAndIsPublicTrue(String userId, Pageable pageable);
    Page<ContentPosting> findByIsPublicTrue(Pageable pageable);


}
