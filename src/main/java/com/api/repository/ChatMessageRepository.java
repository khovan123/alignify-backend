package com.api.repository;

import com.api.model.ChatMessage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Page<ChatMessage> findByChatRoomIdOrderBySendAtAsc(String chatRoomId, Pageable pageable);
    
    Optional<ChatMessage> findTopByChatRoomIdOrderBySendAtDesc(String chatRoomId);

//    @Query(value = "{'roomId': ?0}", sort = "{'sendAt': -1}")
//    Optional<ChatMessage> findLatestByRoomId(String roomId);
}
