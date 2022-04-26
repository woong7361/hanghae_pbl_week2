package pbl.week2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pbl.week2.config.security.PrincipalDetails;
import pbl.week2.config.security.WebSecureConfig;
import pbl.week2.entity.Board;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.service.BoardService;
import pbl.week2.service.FileHandler;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecureConfig.class) })
class BoardControllerTest {

    @Autowired MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean BoardService boardService;

    @Nested
    @DisplayName("게시판 생성 컨트롤러 테스트")
    class createBoardControllerTest {

        @Test
        @config.WithMockCustomUser
        public void test() throws Exception {
            MockMultipartFile multipartFile = new MockMultipartFile("picture", "original", "image/png",new byte[] {});
            BoardDto.PostRes value = new BoardDto.PostRes(1L, "Stirng", "contente", new byte[]{}
                    , 1L, false, LocalDateTime.now());
            BDDMockito.given(boardService.createBoard(any(BoardDto.FileReq.class), anyLong()))
                    .willReturn(value);


            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/posts")
                            .file(multipartFile)
                            .with(csrf())
                            .param("content", "String"))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath("$.postId").value(1L));

            verify(boardService).createBoard(any(BoardDto.FileReq.class), anyLong());
        }
    }
}