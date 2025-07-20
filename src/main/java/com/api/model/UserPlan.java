package com.api.model;

import java.time.ZonedDateTime;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@Document(collection = "userPlans")
public class UserPlan {
    @Id
    private String userPlanId;
    private String userId;
    private String planId;
    private ZonedDateTime createdAt;
    private String status;
    private ZonedDateTime completedAt;

}
