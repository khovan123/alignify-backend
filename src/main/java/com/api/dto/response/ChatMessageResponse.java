package com.api.dto.response;

import com.api.dto.UserDTO;
import com.api.model.ChatMessage;

public class ChatMessageResponse {

    private UserDTO userDTO;
    private ChatMessage message;

    public ChatMessageResponse(UserDTO userDTO, ChatMessage message) {
        this.userDTO = userDTO;
        this.message = message;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

}
