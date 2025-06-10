
package com.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.ContentPosting;

@Repository
public interface IContentPostingRepository extends MongoRepository<ContentPosting, String>{
    List<ContentPosting> findByUserId(String userId);
    List<ContentPosting> findByIsPublicTrue(Pageable pageable);


}
