
package com.api.repository;

import com.api.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BrandRepository extends MongoRepository<Brand, String>{
}
