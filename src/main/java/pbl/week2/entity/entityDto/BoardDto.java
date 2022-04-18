package pbl.week2.entity.entityDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class BoardDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        private String picture;
        @NotBlank
        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class PostIdRes {
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRes {
        private String content;
        private String picture;
        private Long likeCount;
        private boolean isLike;
        private LocalDateTime lastModifiedAt;
    }

}
