package com.api.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlatformRequirementDetailsTrackingRequest {
    List<PostDetailsTracking> postDetailsTrackings;
}
