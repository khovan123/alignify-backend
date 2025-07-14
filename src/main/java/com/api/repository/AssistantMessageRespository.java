package com.api.repository;

import com.api.model.AssistantMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssistantMessageRespository extends MongoRepository<AssistantMessage,String> {
    List<AssistantMessage> findAllByRoomId(String roomId);
}
