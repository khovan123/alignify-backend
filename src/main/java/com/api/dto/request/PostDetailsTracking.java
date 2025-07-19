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

    public PostDetailsTracking(@JsonProperty("platform") String platform,
            @JsonProperty("post_type") String post_type, @JsonProperty("index") int index,
            @JsonProperty("postUrl") String postUrl) {
        this.platform = platform;
        this.post_type = post_type;
        this.index = index;
        this.postUrl = postUrl;
    }

}
