package com.api.repository;

import com.api.model.Category;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findAllByCategoryIdIn(List<String> categoryIds);
}
