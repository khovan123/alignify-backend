package com.api.dto.response;

import com.api.dto.ResponseStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class AssistantResponse<T> {
    private T data;
    private ResponseStatus responseStatus;
}
