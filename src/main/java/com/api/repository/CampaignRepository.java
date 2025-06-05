package com.api.repository;

import com.api.model.Campaign;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {
    Page<Campaign> findAll(Pageable pageable);
    Page<Campaign> findByUserId(String userId, Pageable pageable);

}
