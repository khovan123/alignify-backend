
package com.api.repository;

import com.api.model.ContentPosting;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IContentPostingRepository extends MongoRepository<ContentPosting, String>{
    List<ContentPosting> findByUserId(String userId);
    List<ContentPosting> findByIsPublicTrue();
     

}
