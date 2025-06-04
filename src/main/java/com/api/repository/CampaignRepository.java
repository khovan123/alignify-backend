
package com.api.repository;

import com.api.model.Campaign;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String>{

     List<Campaign> findByUserId(String userId);
      
}
