package com.api.model;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
public class PlatformRequirementDetails {
    private String post_type;
    private int like;
    private int comment;
    private int share;
    private int view;

    public PlatformRequirementDetails() {
        this.like = 0;
        this.comment = 0;
        this.share = 0;
        this.view = 0;
    }

    public PlatformRequirementDetails(String post_type) {
        this.like = 0;
        this.comment = 0;
        this.share = 0;
        this.post_type = post_type;
    }
}