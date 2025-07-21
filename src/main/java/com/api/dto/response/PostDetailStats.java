package com.api.dto.response;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDetailStats {
    private int comment_count;
    private int share_count;
    private int view_count;
    private int play_count;
    private int like_count;
    private String created_at_utc;
    private String thumbnail_url;
    private String video_url;
}
