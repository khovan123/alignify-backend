package com.api.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReadMessagePayload {
    private String messageId;
}