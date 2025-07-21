package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class PostDetailsTracking {

    @JsonProperty("platform")
    private String platform;
    @JsonProperty("post_type")
    private String post_type;
    @JsonProperty("index")
    private int index;
    @JsonProperty("postUrl")
    private String postUrl;
    @JsonProperty("like")
    private int like;
    @JsonProperty("comment")
    private int comment;
    @JsonProperty("share")
    private int share;
    @JsonProperty("view")
    private int view;

    public PostDetailsTracking(
            @JsonProperty("platform") String platform,
            @JsonProperty("post_type") String post_type,
            @JsonProperty("index") int index,
            @JsonProperty("postUrl") String postUrl,
            @JsonProperty("like") int like,
            @JsonProperty("comment") int comment,
            @JsonProperty("share") int share,
            @JsonProperty("view") int view) {
        this.platform = platform;
        this.post_type = post_type;
        this.index = index;
        this.postUrl = postUrl;
        this.like = like;
        this.share = share;
        this.comment = comment;
        this.view = view;
    }

}
