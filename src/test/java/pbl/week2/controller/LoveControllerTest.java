package pbl.week2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import pbl.week2.config.security.WebSecureConfig;
import pbl.week2.service.LoveService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = LoveController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecureConfig.class) })
class LoveControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    LoveService loveService;

    @Test
    @config.WithMockCustomUser
    @DisplayName("좋아요 생성")
    public void createPost() throws Exception {
        //given
        //when
        mockMvc.perform(post("/api/posts/1/like")
                .with(csrf())
        ).andExpect(status().isOk());

        //then
        verify(loveService).createLove(anyLong(), anyLong());
    }
    @Test
    @DisplayName("로그인 없이 좋아요 생성")
    public void createPostFail() throws Exception {
        //given
        //when
        mockMvc.perform(post("/api/posts/1/like")
                .with(csrf())
        ).andExpect(status().isUnauthorized());
        //then
    }

    @Test
    @config.WithMockCustomUser
    @DisplayName("좋아요 삭제")
    public void deletePost() throws Exception {
        //given
        //when
        mockMvc.perform(post("/api/posts/1/like/delete")
                .with(csrf())
        ).andExpect(status().isOk());

        //then
        verify(loveService).deleteLove(anyLong(), anyLong());
    }

    @Test
    @DisplayName("로그인을 안한상태로 좋아요 삭제")
    public void deletePostNotUser() throws Exception {
        //given
        //when
        mockMvc.perform(post("/api/posts/1/like/delete")
                .with(csrf())
        ).andExpect(status().isUnauthorized());

        //then
    }
}