package com.api.repository;

import com.api.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Query("{$or: [{roomOwnerId: ?0}, {members: ?0}]}")
    Page<ChatRoom> findAllByRoomOwnerIdOrMember(String userId, Pageable pageable);

    @Query(value = "{'chatRoomId': ?0, '$or': [{'roomOwnerId': ?1}, {'members': ?1}]}", exists = true)
    boolean existsByChatRoomIdAndRoomOwnerIdOrMember(String chatRoomId, String userId);
}
