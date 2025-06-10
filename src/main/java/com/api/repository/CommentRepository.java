package com.api.repository;

import com.api.model.Comment;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByUserId(String userId, Pageable pageable);

    List<Comment> findByContentId(String contentId, Pageable pageable);

    List<Comment> findAllByContentId(String contentId);

}
