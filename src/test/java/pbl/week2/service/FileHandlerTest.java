package pbl.week2.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.entityDto.BoardDto;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileHandlerTest {

    @InjectMocks FileHandler fileHandler;

    @Nested
    @DisplayName("파일 생성")
    class createFileTest {

        @Test
        @DisplayName("파일 주소 생성하기 테스트")
        public void createFilePath() throws Exception{
            //given
            MultipartFile multipartFile = new MockMultipartFile("test.png", "original", "image/png",new byte[] {});
            //when
            String filePath = fileHandler.getFilePath(multipartFile);
            //then
        }

        @Test
        @DisplayName("파일 생성 테스트")
        public void createFileTest() throws Exception{
            //given
            MultipartFile multipartFile = new MockMultipartFile("test.png", "original", "image/png",new byte[] {});
            //when
            String filePath = fileHandler.getFilePath(multipartFile);
            fileHandler.saveFile(multipartFile, filePath);
            //then

            File file = new File(BoardDto.ABSOLUTE_PATH + filePath);
            if (file.exists()) {
                file.delete();
            }
        }

        @Test
        @DisplayName("다른 확장자 파일")
        public void diffrentContentType() throws Exception{
            //given
            MultipartFile multipartFile = new MockMultipartFile("test.png", "original", "image",new byte[] {});
            //when
            //then
            Assertions.assertThatThrownBy(() -> fileHandler.getFilePath(multipartFile))
                    .isInstanceOf(PblException.class);
        }

        @Test
        @DisplayName("파일이 안들어왔을때")
        public void noMultipartFile() throws Exception{
            //given
            //when
            //then
            Assertions.assertThat(fileHandler.getFilePath(null)).isEqualTo(null);
        }
    }
}