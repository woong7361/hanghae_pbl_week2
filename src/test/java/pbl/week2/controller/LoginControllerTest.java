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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.service.MemberService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock MemberService memberService;
    @InjectMocks LoginController loginController;
    MockMvc mockMvc; // 1
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build(); // 3
    }

    private void mockLoginReqeust(MemberDto.Register registerDto1, ResultMatcher BadRequest) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDto1)))
                .andExpect(BadRequest);
    }

    @Nested
    @DisplayName("로그인 커트롤러 단위 테스트")
    class loginControllerTest {
        @Test
        @DisplayName("username 검증 테스트")
        public void usernameTest() throws Exception {
            //given
            MemberDto.Register registerDto1 = new MemberDto.Register("u", "pwhgf", "nickname");
            MemberDto.Register registerDto2 = new MemberDto.Register("u@#", "pwhgf", "nickname");
            MemberDto.Register registerDto3 = new MemberDto.Register("ugfd#%s1234", "pwhgf", "nickname");
            //when
            //then
            mockLoginReqeust(registerDto1, status().isBadRequest());
            mockLoginReqeust(registerDto2, status().isBadRequest());
            mockLoginReqeust(registerDto3, status().isBadRequest());
        }
        @Test
        public void successTest() throws Exception {
            //given
            MemberDto.Register registerDto = new MemberDto.Register("ugfdsg", "pwgfds", "nickname");

            //when
            //then
            mockLoginReqeust(registerDto, status().isOk());

            verify(memberService).register(registerDto);
        }


        @Test
        @DisplayName("password 검증 테스트")
        public void passwordTest() throws Exception {
            //given
            MemberDto.Register registerDto1 = new MemberDto.Register("woong", "1", "nickname");
            MemberDto.Register registerDto2 = new MemberDto.Register("hjk4323", "  ", "nickname");
            //when
            //then
            mockLoginReqeust(registerDto1, status().isBadRequest());
            mockLoginReqeust(registerDto2, status().isBadRequest());
        }

        @Test
        @DisplayName("nickname 검증 테스트")
        public void nicknameTest() throws Exception {
            //given
            MemberDto.Register registerDto1 = new MemberDto.Register("woong", "1gfds", "fd");
            MemberDto.Register registerDto2 = new MemberDto.Register("hjk4323", "  gfds", "^&*(");
            MemberDto.Register registerDto3 = new MemberDto.Register("hjk4323", "  gfds", "  hj");
            //when
            //then
            mockLoginReqeust(registerDto1, status().isBadRequest());
            mockLoginReqeust(registerDto2, status().isBadRequest());
            mockLoginReqeust(registerDto3, status().isBadRequest());
        }


    }

}

