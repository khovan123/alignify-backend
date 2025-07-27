package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Page<ChatMessage> findByChatRoomIdOrderBySendAtDesc(String chatRoomId, Pageable pageable);

    List<ChatMessage> findByChatRoomIdOrderBySendAtAsc(String chatRoomId);

    Optional<ChatMessage> findTopByChatRoomIdOrderBySendAtDesc(String chatRoomId);

    void deleteAllByChatRoomId(String chatRoomId);

    // @Query(value = "{'roomId': ?0}", sort = "{'sendAt': -1}")
    // Optional<ChatMessage> findLatestByRoomId(String roomId);
}
