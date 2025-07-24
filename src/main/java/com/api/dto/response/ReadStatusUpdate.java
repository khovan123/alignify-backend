package com.api.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusUpdate {
    private String messageId;
    private List<String> readBy;
}
