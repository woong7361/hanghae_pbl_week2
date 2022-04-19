package pbl.week2.entity.entityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class BoardDto {
    public static String ABSOLUTE_PATH =
            new File("").getAbsolutePath() + File.separator + File.separator;

    @Data
    @AllArgsConstructor
    public static class PostIdRes {
        private Long postId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostRes {
        private String content;
        private byte[] picture;
        private Long likeCount;
        private boolean isLike;
        private LocalDateTime lastModifiedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileReq {
        private MultipartFile picture = null;
        @NotBlank
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileDto {
        private String originalFileName;
        private String filePath;
    }
}
