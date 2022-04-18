package pbl.week2.entity.entityDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.time.LocalDateTime;

public class BoardDto {
    public static String BASE_DIR =
            File.pathSeparator + "home" + File.separator + "woong" + File.separator + "code" + File.separator
                    + "sparata" + File.separator + "temp";

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileReq {
        private MultipartFile picture;
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
