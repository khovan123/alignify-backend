package com.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Query("{$or: [{roomOwnerId: ?0}, {members: ?0}]}")
    Page<ChatRoom> findAllByRoomOwnerIdOrMember(String userId, Pageable pageable);

    @Query(value = "{'chatRoomId': ?0, '$or': [{'roomOwnerId': ?1}, {'members': ?1}]}", exists = true)
    boolean existsByChatRoomIdAndRoomOwnerIdOrMember(String chatRoomId, String userId);

    @Query(value = "{$or: [{roomOwnerId: ?0}, {members: ?0}]}", sort = "{'createdAt': -1}")
    Page<ChatRoom> findAllByRoomOwnerIdOrMemberOrderByCreatedAtDesc(String userId, Pageable pageable);

    int countByRoomOwnerId(String userId);
}
