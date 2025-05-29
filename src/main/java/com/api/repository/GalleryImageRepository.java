package com.api.repository;

import com.api.model.GalleryImage;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryImageRepository extends MongoRepository<GalleryImage, String> {

    @Query(value = "{ '_id': { $in: ?0 } }", sort = "{ 'createAt': -1 }")
    List<GalleryImage> findTop9ByIdInOrderByUploadedAtDesc(List<String> ids, Pageable pageable);
}
