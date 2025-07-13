package com.api.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class AssistantRequest {
    private String question;
}
