package com.api.repository;

import com.api.model.Image;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

    @Query(value = "{ '_id': { $in: ?0 } }", sort = "{ 'createAt': -1 }")
    List<Image> findTop9ByIdInOrderByUploadedAtDesc(List<String> ids, Pageable pageable);
}
