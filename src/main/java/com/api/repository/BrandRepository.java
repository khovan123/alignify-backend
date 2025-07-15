package com.api.repository;

import com.api.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {

    Page<Brand> findByCategoryIdsInOrderByTotalCampaignDesc(String category, Pageable pageable);

}
