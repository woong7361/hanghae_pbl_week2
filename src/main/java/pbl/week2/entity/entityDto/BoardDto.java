package pbl.week2.entity.entityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import pbl.week2.entity.Board;
import pbl.week2.service.FileHandler;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        private Long postId;
        private String nickname;
        private String content;
        private byte[] picture;
        private Long likeCount;
        private boolean isLike;
        private LocalDateTime modifiedAt;

        public PostRes(Board board, FileHandler fileHandler, List<Long> myLikeBoardId) {
            this.postId = board.getId();
            this.nickname = board.getMember().getNickname();
            this.content = board.getContent();
            this.picture = fileHandler.getFileToByte(board.getPicture());
            this.likeCount = board.getLikeCount();
            this.isLike = myLikeBoardId.stream().anyMatch(boardId -> boardId.equals(board.getId()));
            this.modifiedAt = board.getModifiedAt();
        }

        public PostRes(Board board, FileHandler fileHandler) {
            this.postId = board.getId();
            this.nickname = board.getMember().getNickname();
            this.content = board.getContent();
            this.picture = fileHandler.getFileToByte(board.getPicture());
            this.likeCount = board.getLikeCount();
            this.isLike = false;
            this.modifiedAt = board.getModifiedAt();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResList {
        private Slice<PostRes> posts;
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
