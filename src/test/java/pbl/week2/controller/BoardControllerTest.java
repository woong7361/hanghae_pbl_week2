package pbl.week2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.service.BoardService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Mock BoardService boardService;
    @InjectMocks BoardController boardController;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build(); // 3
    }


    @Nested
    @DisplayName("게시판 생성 컨트롤러 테스트")
    class createBoardControllerTest {
//
//        @Test
//        @DisplayName("게시판 생성")
//        public void createSuccessTest() throws Exception{
//            //given
//            MockMultipartFile multipartFile = new MockMultipartFile("test.png", "original", "image/png",new byte[] {});
//            //when
//            mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
//                                    .contentType(MediaType.MULTIPART_FORM_DATA)
//                                    .content(objectMapper.writeValueAsString(new BoardDto.FileReq(multipartFile, "content"))))
//                    .andExpect(MockMvcResultMatchers.status().isOk());
//            //then
//        }

    }
}